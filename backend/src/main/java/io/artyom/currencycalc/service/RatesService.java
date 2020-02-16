package io.artyom.currencycalc.service;

import io.artyom.currencycalc.entity.CurrencyPairEntity;
import io.artyom.currencycalc.entity.RateEntity;
import io.artyom.currencycalc.repo.RateRepo;
import io.artyom.currencycalc.service.rates.RatesProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Currency;

@Service
@AllArgsConstructor
@Slf4j
public class RatesService {

    private final RatesProvider ratesProviderService;
    private final RateRepo rateRepo;
    private final CurrencyService currencyService;
    private final CurrencyPairService currencyPairService;

    @Transactional
    public void refreshRates() {
        log.info("start refresh rates");

        for (Currency baseCurrency : currencyService.findAll()) {
            try {
                ratesProviderService.getRates(baseCurrency).forEach((quoteCurrency, rate) -> {
                    if (!baseCurrency.equals(quoteCurrency)) {
                        var pairName = currencyPairService.buildPairName(baseCurrency, quoteCurrency);
                        var currencyPairOpt = currencyPairService.getByPairName(pairName);
                        if (currencyPairOpt.isPresent()) {
                            var currencyPair = currencyPairOpt.get();
                            var rateEntity = currencyPair.getRate();
                            if (rateEntity == null){
                                rateEntity = new RateEntity();
                                rateEntity.setCreated(Instant.now());
                                rateEntity.setRate(rate);
                                currencyPair.setRate(rateEntity);
                                currencyPairService.save(currencyPair);
                            }else{
                                rateEntity.setCreated(Instant.now());
                                rateEntity.setRate(rate);
                                rateRepo.save(rateEntity);
                            }
                        } else {
                            log.info("new currency pair:{}", pairName);
                            CurrencyPairEntity pair = new CurrencyPairEntity();
                            pair.setBase(baseCurrency);
                            pair.setQuote(quoteCurrency);
                            pair.setFee(null);
                            pair.setName(pairName);

                            var rateEntity = new RateEntity();
                            rateEntity.setRate(rate);
                            pair.setRate(rateEntity);

                            currencyPairService.save(pair);
                        }
                    }
                });
            } catch (Exception ex) {
                log.error("refreshRates(). Err:" + ex.getMessage(), ex);
            }
        }

        log.info("end refresh. Rate count:{}", rateRepo.count());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void saveRate(RateEntity rateEntity){
        rateRepo.save(rateEntity);
    }
}
