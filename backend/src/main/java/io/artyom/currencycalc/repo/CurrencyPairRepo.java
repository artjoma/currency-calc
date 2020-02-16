package io.artyom.currencycalc.repo;

import io.artyom.currencycalc.entity.CurrencyPairEntity;
import io.artyom.currencycalc.util.AppException;
import io.artyom.currencycalc.util.AppExceptionKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyPairRepo extends JpaRepository <CurrencyPairEntity, Long> {

    default CurrencyPairEntity get(String pairName) throws AppException{
        return getByName(pairName).orElseThrow(() -> new AppException(AppExceptionKind.CURRENCY_PAIR_NOT_FOUND));
    }

    Optional<CurrencyPairEntity> getByName(String name);


}
