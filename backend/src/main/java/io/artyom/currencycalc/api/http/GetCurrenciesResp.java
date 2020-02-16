package io.artyom.currencycalc.api.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCurrenciesResp extends ErrResp {

    private Set<Currency> result;

}
