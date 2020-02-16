package io.artyom.currencycalc.repo;

import io.artyom.currencycalc.entity.RateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepo extends JpaRepository<RateEntity, Long> {

}
