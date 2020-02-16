package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.dto.CurrencyPairDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCurrencyPairsResp extends ErrResp {

    private List<CurrencyPairDto> result;

}
