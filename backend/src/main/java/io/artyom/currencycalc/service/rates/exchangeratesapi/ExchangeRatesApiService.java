package io.artyom.currencycalc.service.rates.exchangeratesapi;

import io.artyom.currencycalc.service.rates.RatesProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
//exchangeratesapi impl
public class ExchangeRatesApiService extends RatesProvider {

    private static final String API_URL = "https://api.exchangeratesapi.io/";

    private RatesResp call(String uriPath) throws Exception{
        RatesResp ratesResp = null;
        var now = System.nanoTime();
        try {
            return (RatesResp)super.call(API_URL + uriPath, RatesResp.class);
        }catch(Exception ex){
            log.error("end call. Took:{}ns", System.nanoTime() - now);
            log.error("call. Err:" + ex.getMessage(), ex);
            throw ex;
        }
    }

    public Set<Currency> getCurrencies() throws Exception{
        var ratesResp = call("latest");
        var currencies = new HashSet<Currency>(100);
        currencies.add(Currency.getInstance(ratesResp.getBase()));
        ratesResp.getRates().keySet().forEach(currency ->{
            currencies.add(currency);
        });

        return currencies;
    }

    //not implemented yet, look on getRates() method for bulk read
    public BigDecimal getPairRate(Currency base, Currency quote) throws Exception{
       throw new Exception("not implemented");
    }

    @Override
    public Map<Currency, BigDecimal> getRates(Currency base) throws Exception{
        var ratesResp = call("latest?base="+base.getCurrencyCode());
        if (StringUtils.isEmpty(ratesResp.getError())){
            return ratesResp.getRates();
        }else{
            throw new Exception("Rate API return err:" + ratesResp.getError());
        }
    }

}
