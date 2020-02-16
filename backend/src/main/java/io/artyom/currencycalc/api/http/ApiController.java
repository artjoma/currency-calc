package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.api.converters.CurrencyPairConverter;
import io.artyom.currencycalc.entity.CurrencyPairEntity;
import io.artyom.currencycalc.service.CurrencyPairService;
import io.artyom.currencycalc.service.CurrencyService;
import io.artyom.currencycalc.service.FeesService;
import io.artyom.currencycalc.util.AppException;
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
    private final FeesService feesService;

    @GetMapping("/currencies")
    public GetCurrenciesResp getAllCurrencies() {
        return new GetCurrenciesResp(currencyService.findAll());
    }

    @GetMapping("/pairs")
    public GetCurrencyPairsResp allFees() {
        var pairs = currencyPairService.findAll();
        var sortedPairs = pairs.stream()
                .filter(pair -> pair.getFee() != null)
                .filter(pair -> pair.getFee().getFee().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(CurrencyPairEntity::getName))
                .map(CurrencyPairConverter::convert)
                .collect(Collectors.toList());

        return new GetCurrencyPairsResp(sortedPairs);
    }

    @PutMapping("/fees")
    public SuccessResp setFee(@RequestBody SetFeeReq req) throws AppException{
        feesService.setFee(req.getPairName(), req.getFeeAmount(), req.getReqId());
        return new SuccessResp();
    }

    @PostMapping("/conversion")
    public ConversationResp conversion(@RequestBody ConversionReq req) throws AppException{
        var result = currencyPairService.
                conversion(req.getPairName(), req.getAmount(), req.getReqId());
        return new ConversationResp(result);
    }

}
