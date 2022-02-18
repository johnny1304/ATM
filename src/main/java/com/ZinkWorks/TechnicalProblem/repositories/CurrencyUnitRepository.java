package com.ZinkWorks.TechnicalProblem.repositories;

import com.ZinkWorks.TechnicalProblem.entities.CurrencyUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CurrencyUnitRepository extends JpaRepository<CurrencyUnit, Integer>{

    @Query("SELECT SUM(UNIT*QUANTITY) FROM CurrencyUnit")
    BigDecimal getTotalAvailableCashAmount();

    @Query("SELECT MIN(unit) FROM CurrencyUnit")
    Integer getMinimumCurrencyUnit();

}
