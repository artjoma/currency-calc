package io.artyom.currencycalc.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConversionDto {
    private BigDecimal amount;
    private BigDecimal fee;
    private BigDecimal rate;
}
