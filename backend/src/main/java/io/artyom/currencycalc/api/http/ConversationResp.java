package io.artyom.currencycalc.api.http;

import io.artyom.currencycalc.dto.ConversionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationResp extends ErrResp{

    private ConversionDto result;

}
