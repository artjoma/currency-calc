package io.artyom.currencycalc.service;

import io.artyom.currencycalc.dto.ConversionDto;
import io.artyom.currencycalc.entity.CurrencyPair;
import io.artyom.currencycalc.repo.CurrencyPairRepo;
import io.artyom.currencycalc.service.rates.RatesProvider;
import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyPairService {
    public static final int APP_MONEY_SCALE = 6;

    private final RatesProvider ratesProviderService;
    private final CurrencyPairRepo currencyPairRepo;
    private final CurrencyService currencyService;

    @PostConstruct
    //stop application initialization if exception happens
    public void init() throws Exception {
        currencyService.addAll(ratesProviderService.getCurrencies());
        //build currency pairs
        var currencies = currencyService.findAll();
        for (Currency base : currencies) {
            for (Currency quote : currencies) {
                log.info("create:{} {}", base.getCurrencyCode(), quote.getCurrencyCode());
                if (!base.equals(quote)) {
                    currencyPairRepo.add(new CurrencyPair(base, quote, BigDecimal.ZERO, BigDecimal.ZERO));
                }
            }
        }
        log.info("currency pairs count:{}, currency count:{}", currencyPairRepo.count(), currencyService.count());
        //set mock fee data
        currencyPairRepo.get("EURUSD").get().setFee(BigDecimal.valueOf(0.02));
        refreshRates();
    }

    @Scheduled(cron = "0 * * * * *")
    public void periodicRateUpdate() {
        try {
            refreshRates();
        } catch (Exception err) {
            log.error("periodicRateUpdate err:{}", err.getMessage());
        }
    }

    private void refreshRates() {
        log.info("start refresh rates");

        for (Currency baseCurrency : currencyService.findAll()) {
            try {
                ratesProviderService.getRates(baseCurrency).forEach((quoteCurrency, rate) -> {
                    if (!baseCurrency.equals(quoteCurrency)) {
                        var currencyPairOpt = currencyPairRepo.get(CurrencyPair.name(baseCurrency, quoteCurrency));
                        if (currencyPairOpt.isPresent()) {
                            currencyPairOpt.get().setRate(rate);
                        } else {
                            currencyPairRepo.add(new CurrencyPair(baseCurrency, quoteCurrency, rate, BigDecimal.ZERO));
                        }
                        //we should check if currency pair registered at repo
                        currencyPairOpt.ifPresentOrElse(
                                (val)
                                        -> {
                                    val.setRate(rate);
                                },
                                ()
                                        -> {
                                    currencyPairRepo.add(new CurrencyPair(baseCurrency, quoteCurrency, rate, BigDecimal.ZERO));
                                });
                    }
                });
            } catch (Exception ex) {
                log.error("refreshRates(). Err:" + ex.getMessage(), ex);
            }
        }

        log.info("end refresh. Currency pairs:{}", currencyPairRepo.count());
    }

    public void setFee(CurrencyPair pair, BigDecimal feeAmount, long reqId) throws AppException {
        log.info("set fee:{} - {} reqId:{}", pair.getName(), feeAmount.toPlainString(), reqId);
        if (feeAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new AppException(AppExceptionKind.INVALID_FEE_AMOUNT);
        }
        pair.setFee(feeAmount);
    }

    //result = amount X (rate - fee)
    public ConversionDto conversion(CurrencyPair pair, BigDecimal amount, long reqId) throws AppException {
        if (amount.compareTo(BigDecimal.ZERO) < 1){
            throw new AppException(AppExceptionKind.INVALID_AMOUNT);
        }
        var fee = pair.getFee();
        var rate = pair.getRate();
        log.info("conversion:{} - {} {} {} reqId:{}", pair.getName(), fee, amount, rate, reqId);
        var resultAmount = rate.subtract(fee).multiply(amount)
                .setScale(APP_MONEY_SCALE, RoundingMode.HALF_EVEN);
        return ConversionDto.builder().amount(resultAmount).fee(fee).rate(rate).build();
    }

    public Collection<CurrencyPair> findAll() {
        return currencyPairRepo.findAll();
    }

    public Optional<CurrencyPair> get(String currencyPairName){
        return currencyPairRepo.get(currencyPairName);
    }

}
