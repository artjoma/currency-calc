package io.artyom.currencycalc.service.rates.exchangeratesapi;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Map;

@Data
public class RatesResp {
    private Map<Currency, BigDecimal> rates;
    private String base;
    private Date date;

    private String error;
}
