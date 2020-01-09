package io.artyom.currencycalc.service.rates;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Currency;
import java.util.Map;
import java.util.Set;

public abstract class RatesProvider{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Object call(String uri, Class clazz) throws Exception{
        return objectMapper.readValue(new URL(uri), clazz);
    }

    public abstract Set<Currency> getCurrencies() throws Exception;

    public abstract BigDecimal getPairRate(Currency base, Currency quote) throws Exception;

    public abstract Map<Currency, BigDecimal> getRates(Currency base) throws Exception;

}
