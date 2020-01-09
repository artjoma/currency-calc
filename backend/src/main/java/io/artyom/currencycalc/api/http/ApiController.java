package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.entity.CurrencyPair;
import io.artyom.currencycalc.service.CurrencyPairService;
import io.artyom.currencycalc.service.CurrencyService;
import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adm/v1")
@RequiredArgsConstructor
public class ApiController {

    private final CurrencyPairService currencyPairService;
    private final CurrencyService currencyService;

    @GetMapping("/currencies")
    public GetCurrenciesResp getAllCurrencies() {
        return new GetCurrenciesResp(currencyService.findAll());
    }

    @GetMapping("/pairs")
    public GetCurrencyPairsResp allFees() {
        var pairs = currencyPairService.findAll();
        var sortedPairs = pairs.stream().filter(pair -> pair.getFee().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(CurrencyPair::getFee))
                .collect(Collectors.toList());

        return new GetCurrencyPairsResp(sortedPairs);
    }

    @PutMapping("/fees")
    public SuccessResp setFee(@RequestBody SetFeeReq req) throws AppException{
        currencyPairService.setFee(validatePairName(req.getPairName()), req.getFeeAmount(), req.getReqId());
        return new SuccessResp();
    }

    @PostMapping("/conversion")
    public ConversationResp conversion(@RequestBody ConversionReq req) throws AppException{
        var result = currencyPairService.
                conversion(validatePairName(req.getPairName()), req.getAmount(), req.getReqId());
        return new ConversationResp(result);
    }

    private CurrencyPair validatePairName(String pairName) throws AppException{
        var pairOpt = currencyPairService.get(pairName);
        if (pairOpt.isPresent()){
            return pairOpt.get();
        }else{
            throw new AppException(AppExceptionKind.CURRENCY_PAIR_NOT_FOUND);
        }
    }
}
