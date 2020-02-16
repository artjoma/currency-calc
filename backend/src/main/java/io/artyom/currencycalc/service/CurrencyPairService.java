package io.artyom.currencycalc.service;

import io.artyom.currencycalc.dto.ConversionDto;
import io.artyom.currencycalc.entity.CurrencyPairEntity;
import io.artyom.currencycalc.repo.CurrencyPairRepo;
import io.artyom.currencycalc.service.rates.RatesProvider;
import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.List;
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
    @Transactional
    //stop application initialization if exception happens
    public void init() throws Exception {
        currencyService.addAll(ratesProviderService.getCurrencies());
        //build currency pairs
        var currencies = currencyService.findAll();
        for (Currency base : currencies) {
            for (Currency quote : currencies) {
                if (!base.equals(quote)) {
                    var pairName = buildPairName(base, quote);
                    var entityOpt = currencyPairRepo.getByName(pairName);
                    if (entityOpt.isEmpty()){
                        log.info("currency pair not found: {}", pairName);
                        CurrencyPairEntity pair = new CurrencyPairEntity();
                        pair.setBase(base);
                        pair.setQuote(quote);
                        pair.setFee(null);
                        pair.setName(pairName);

                        currencyPairRepo.save(pair);
                        log.info("insert ok id:{}", pair.getId());
                    }else {
                        log.info("currency pair already present:{}", pairName);
                    }
                }
            }
        }
        log.info("currency pairs count:{}, currency count:{}", currencyPairRepo.count(), currencyService.count());


    }

    //result = amount X (rate - fee)
    public ConversionDto conversion(String pairName, BigDecimal amount, long reqId) throws AppException {
        if (amount.compareTo(BigDecimal.ZERO) < 1){
            throw new AppException(AppExceptionKind.INVALID_AMOUNT);
        }
        var pair = currencyPairRepo.get(pairName);
        var fee = pair.getFee().getFee();
        var rate = pair.getRate().getRate();
        log.info("conversion:{} - {} {} {} reqId:{}", pairName, fee, amount, rate, reqId);
        var resultAmount = rate.subtract(fee).multiply(amount)
                .setScale(APP_MONEY_SCALE, RoundingMode.HALF_EVEN);
        return ConversionDto.builder().amount(resultAmount).fee(fee).rate(rate).build();
    }

    public List<CurrencyPairEntity> findAll() {
        return currencyPairRepo.findAll();
    }

    public CurrencyPairEntity get(String currencyPairName) throws AppException{
        return currencyPairRepo.get(currencyPairName);
    }

    public String buildPairName(Currency base, Currency quote){
        return base.getCurrencyCode() + quote.getCurrencyCode();
    }

    public Optional<CurrencyPairEntity> getByPairName(String pairName){
        return currencyPairRepo.getByName(pairName);
    }

    @Transactional
    public void save(CurrencyPairEntity currencyPairEntity){
        currencyPairRepo.save(currencyPairEntity);
    }
}
