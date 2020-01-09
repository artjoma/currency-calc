package io.artyom.currencycalc.util;

import lombok.Data;

@Data
public class AppException extends Exception{
    private AppExceptionKind apiErr;

    public AppException(AppExceptionKind apiErr){
        this.apiErr = apiErr;
    }
}
