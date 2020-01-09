package io.artyom.currencycalc;

import io.artyom.currencycalc.api.http.ConversationResp;
import io.artyom.currencycalc.api.http.ConversionReq;
import io.artyom.currencycalc.api.http.ErrResp;
import io.artyom.currencycalc.api.http.GetCurrenciesResp;
import io.artyom.currencycalc.api.http.GetCurrencyPairsResp;
import io.artyom.currencycalc.api.http.SetFeeReq;
import io.artyom.currencycalc.entity.CurrencyPair;
import io.artyom.currencycalc.util.AppExceptionKind;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

//currency pair integration tests
public class CurrencyPairIT extends AbstractCurrencyCalcApplicationTest{

    public static final int APP_MONEY_SCALE = 6;

    @Test
    public void getCurrencies(){
        GetCurrenciesResp response = restTemplate.getForObject(getApiURI() + "currencies", GetCurrenciesResp.class);
        var currencies = response.getResult();
        Assert.assertTrue(!currencies.isEmpty());
        Assert.assertNull(response.getErr());
        Assert.assertTrue(currencies.contains(Currency.getInstance("EUR")));
    }

    @Test
    public void getPairs(){
        GetCurrencyPairsResp response = restTemplate.getForObject(getApiURI() + "pairs", GetCurrencyPairsResp.class);
        var pairs = response.getResult();
        Assert.assertTrue(!pairs.isEmpty());
        Assert.assertNull(response.getErr());
        CurrencyPair pair = pairs.get(0);
        Assert.assertNotNull(pair.getBase());
        Assert.assertNotNull(pair.getQuote());
        Assert.assertNotNull(pair.getName());
        Assert.assertNotNull(pair.getRate());
        Assert.assertNotNull(pair.getFee());
    }

    @Test
    public void setFees(){
        var pairName = "USDEUR";
        var fee = BigDecimal.valueOf(0.03d);

        var request = new SetFeeReq(pairName, fee);
        request.setReqId(Long.MAX_VALUE);
        restTemplate.put(getApiURI() + "fees", request);

        GetCurrencyPairsResp response = restTemplate.getForObject(getApiURI() + "pairs", GetCurrencyPairsResp.class);
        var pair = response.getCurrencyPairByName(pairName);
        Assert.assertEquals(fee, pair.getFee());
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
        Assert.assertNotNull(convInfo.getAmount());
        Assert.assertNotNull(convInfo.getFee());

        var resultAmount = amount.multiply(convInfo.getRate().subtract(convInfo.getFee())).setScale(APP_MONEY_SCALE, RoundingMode.HALF_EVEN);
        Assert.assertEquals(resultAmount, convInfo.getAmount());
    }

    @Test
    public void conversionErr(){
        var request = new ConversionReq();
        request.setAmount(BigDecimal.valueOf(10.0));
        request.setPairName("USDEUR1");
        request.setReqId(Long.MAX_VALUE);
        ErrResp response = restTemplate.postForObject(getApiURI() + "conversion", request, ConversationResp.class);

        var err = response.getErr();
        Assert.assertNotNull(err.getMsg());
        Assert.assertEquals(err.getCode(), AppExceptionKind.CURRENCY_PAIR_NOT_FOUND.getCode());
    }

}
