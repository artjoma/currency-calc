package io.artyom.currencycalc.api.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetFeeReq extends BaseRequest{

    private String pairName;
    private BigDecimal feeAmount;

}
