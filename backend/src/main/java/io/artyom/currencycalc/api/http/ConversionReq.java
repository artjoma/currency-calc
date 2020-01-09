package io.artyom.currencycalc.api.http;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ConversionReq extends BaseRequest{

    private String pairName;
    private BigDecimal amount;

}
