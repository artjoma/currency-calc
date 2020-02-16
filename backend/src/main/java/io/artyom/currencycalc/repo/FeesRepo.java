package io.artyom.currencycalc.repo;

import io.artyom.currencycalc.entity.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeesRepo extends JpaRepository<FeeEntity, Long> {

    Optional<FeeEntity> getByPair(long pair);

}
