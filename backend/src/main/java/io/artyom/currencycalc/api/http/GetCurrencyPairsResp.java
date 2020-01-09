package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.entity.CurrencyPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCurrencyPairsResp extends ErrResp {

    private List<CurrencyPair> result;

    //return null if not found
    public CurrencyPair getCurrencyPairByName(String pairName){
        for (CurrencyPair pair : result){
            if (pair.getName().equals(pairName)){
                return pair;
            }
        }

        return null;
    }

}
