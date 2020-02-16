package io.artyom.currencycalc.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class CurrencyPairDto {
    private long id;
    private Currency base;
    private Currency quote;
    private BigDecimal fee;
    private BigDecimal rate;
    private String name;
}
