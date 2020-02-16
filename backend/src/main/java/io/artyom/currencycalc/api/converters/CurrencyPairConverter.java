package io.artyom.currencycalc.api.converters;

import io.artyom.currencycalc.dto.CurrencyPairDto;
import io.artyom.currencycalc.entity.CurrencyPairEntity;

public class CurrencyPairConverter {

    public static CurrencyPairDto convert(CurrencyPairEntity entity){
        return CurrencyPairDto.builder()
                .base(entity.getBase())
                .quote(entity.getQuote())
                .fee(entity.getFee().getFee())
                .rate(entity.getRate().getRate())
                .id(entity.getId())
                .name(entity.getName()).build();
    }

}
