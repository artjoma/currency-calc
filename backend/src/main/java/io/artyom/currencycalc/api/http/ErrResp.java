package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrResp {

    protected AppExceptionKind err;

}
