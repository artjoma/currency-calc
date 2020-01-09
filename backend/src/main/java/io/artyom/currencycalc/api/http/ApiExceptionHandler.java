package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrResp> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("err:" + ex.getMessage(), ex);

        if (ex instanceof AppException){
            var apErr = (AppException) ex;
            log.error("err:{} msg:{}", apErr.getApiErr().name(), apErr.getApiErr().getMsg());
            return new ResponseEntity<>(new ErrResp(apErr.getApiErr()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new ErrResp(AppExceptionKind.SERVICE_UNAVAILABLE), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
