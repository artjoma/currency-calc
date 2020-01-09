package io.artyom.currencycalc.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppExceptionKind {

    CURRENCY_PAIR_NOT_AVAILABLE(1, "Currency pair not available. Please try again later"),
    CURRENCY_PAIR_NOT_FOUND(2, "Currency pair not found"),
    INVALID_AMOUNT(3, "Invalid amount"),
    INVALID_FEE_AMOUNT(4, "Invalid fee amount"),
    SERVICE_UNAVAILABLE(4, "Service unavailable");

    private int code;
    private String msg;

    AppExceptionKind(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

    @JsonCreator
    public static AppExceptionKind forValue(int code, String msg) {
        for (AppExceptionKind kind : AppExceptionKind.values()){
            if (kind.getCode() == code && kind.getMsg().equals(msg)){
                return kind;
            }
        }
        return null;
    }

}
