package io.artyom.currencycalc.api.http;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResp extends ErrResp {

    private String result;

    public SuccessResp(){
        this.result = "success";
    }
}
