package io.artyom.currencycalc;

import io.artyom.currencycalc.api.http.ConversationResp;
import io.artyom.currencycalc.api.http.ConversionReq;
import io.artyom.currencycalc.api.http.ErrResp;
import io.artyom.currencycalc.api.http.GetCurrenciesResp;
import io.artyom.currencycalc.api.http.GetCurrencyPairsResp;
import io.artyom.currencycalc.api.http.SetFeeReq;
import io.artyom.currencycalc.dto.CurrencyPairDto;
import io.artyom.currencycalc.util.AppExceptionKind;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

//currency pair integration tests
public class CurrencyPairIT extends AbstractCurrencyCalcApplicationTest{

    public static final int APP_MONEY_SCALE = 6;

    @Test
    public void getCurrencies(){
        GetCurrenciesResp response = restTemplate.getForObject(getApiURI() + "currencies", GetCurrenciesResp.class);
        var currencies = response.getResult();
        assertTrue(!currencies.isEmpty());
        assertNull(response.getErr());
        assertTrue(currencies.contains(Currency.getInstance("EUR")));
    }

    @Test
    public void getPairs(){
        GetCurrencyPairsResp response = restTemplate.getForObject(getApiURI() + "pairs", GetCurrencyPairsResp.class);
        var pairs = response.getResult();
        assertTrue(!pairs.isEmpty());
        assertNull(response.getErr());
        assertThat(pairs)
                .hasSizeGreaterThan(0)
                .extracting(CurrencyPairDto::getName)
                .containsExactlyInAnyOrder("EURUSD");

        var pair = pairs.get(0);
        assertNotNull(pair.getBase());
        assertNotNull(pair.getQuote());
        assertNotNull(pair.getName());
    }

    @Test
    public void setFees(){
        var pairName = "USDEUR";
        var fee = BigDecimal.valueOf(0.04d).setScale(APP_MONEY_SCALE);

        var request = new SetFeeReq(pairName, fee);
        request.setReqId(Long.MAX_VALUE);
        restTemplate.put(getApiURI() + "fees", request);

        GetCurrencyPairsResp response = restTemplate.getForObject(getApiURI() + "pairs", GetCurrencyPairsResp.class);
        assertThat(response.getResult())
                .extracting(CurrencyPairDto::getName, CurrencyPairDto::getFee)
                .containsAnyOf(
                        tuple(pairName, fee)
                );
    }

    @Test
    public void conversion(){
        setFees();
        var amount = BigDecimal.valueOf(10.0);
        var request = new ConversionReq();
        request.setAmount(amount);
        request.setPairName("USDEUR");
        request.setReqId(Long.MAX_VALUE);
        ConversationResp response = restTemplate.postForObject(getApiURI() + "conversion", request, ConversationResp.class);

        var convInfo = response.getResult();
        assertNotNull(convInfo.getAmount());
        assertNotNull(convInfo.getFee());

        var resultAmount = amount.multiply(convInfo.getRate().subtract(convInfo.getFee())).setScale(APP_MONEY_SCALE, RoundingMode.HALF_EVEN);
        assertEquals(resultAmount, convInfo.getAmount());
    }

    @Test
    public void conversionErr(){
        var request = new ConversionReq();
        request.setAmount(BigDecimal.valueOf(10.0));
        request.setPairName("USDEUR1");
        request.setReqId(Long.MAX_VALUE);
        ErrResp response = restTemplate.postForObject(getApiURI() + "conversion", request, ConversationResp.class);

        var err = response.getErr();
        assertNotNull(err.getMsg());
        assertEquals(err.getCode(), AppExceptionKind.CURRENCY_PAIR_NOT_FOUND.getCode());
    }

}
