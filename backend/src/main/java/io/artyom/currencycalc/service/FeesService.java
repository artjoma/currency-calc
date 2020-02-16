package io.artyom.currencycalc.service;

import io.artyom.currencycalc.entity.FeeEntity;
import io.artyom.currencycalc.repo.FeesRepo;
import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class FeesService {

    private final FeesRepo feesRepo;
    private final CurrencyPairService currencyPairService;

    @PostConstruct
    public void init() throws Exception{
        //set mock fee data
        var currencyPairEntity = currencyPairService.get("EURUSD");
        if (currencyPairEntity.getFee() == null){
            FeeEntity feeEntity = new FeeEntity();
            feeEntity.setFee(BigDecimal.valueOf(0.02));
            feeEntity.setPair(currencyPairEntity);
            feeEntity.setUpdated(Instant.now());
            currencyPairEntity.setFee(feeEntity);
            currencyPairService.save(currencyPairEntity);
        }else{
            log.info("EURUSD fee already present");
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void saveFee(FeeEntity feeEntity){
        feesRepo.save(feeEntity);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void setFee(String pairName, BigDecimal feeAmount, long reqId) throws AppException {
        log.info("set fee:{} - {} reqId:{}", pairName, feeAmount.toPlainString(), reqId);
        if (feeAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new AppException(AppExceptionKind.INVALID_FEE_AMOUNT);
        }
        var pair = currencyPairService.get(pairName);
        var feeEntity = pair.getFee();
        if (feeEntity == null){
            feeEntity = new FeeEntity();
            feeEntity.setPair(pair);
            feeEntity.setUpdated(Instant.now());
        }
        feeEntity.setFee(feeAmount);

        feesRepo.save(feeEntity);
        pair.setFee(feeEntity);
    }

}
