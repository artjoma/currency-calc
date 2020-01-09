package io.artyom.currencycalc.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Data
@ToString
public class CurrencyPair {

    private Currency base;
    private Currency quote;
    private BigDecimal rate;
    private BigDecimal fee;
    private String name;
    private Instant rateUpdatedAt;
    private Instant feeUpdatedAt;

    public CurrencyPair(Currency base, Currency quote, BigDecimal rate, BigDecimal fee){
        this.base = base;
        this.quote = quote;
        this.setRate(rate);
        this.setFee(fee);
        this.name = name(base, quote);
    }

    public final static String name(Currency base, Currency quote){
        return base.getCurrencyCode() + quote.getCurrencyCode();
    }

    public void setRate(BigDecimal rate){
        this.rate = rate;
        this.rateUpdatedAt = Instant.now();
    }

    public void setFee(BigDecimal fee){
        this.fee = fee;
        this.feeUpdatedAt = Instant.now();
    }
}
