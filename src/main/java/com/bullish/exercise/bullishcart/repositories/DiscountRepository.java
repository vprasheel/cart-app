package com.bullish.exercise.bullishcart.repositories;

import com.bullish.exercise.bullishcart.entities.Discount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long> {

    @Query("SELECT d FROM Discount d WHERE d.productId = ?1 and CURRENT_DATE between d.dealStartDate and d.dealEndDate")
    Collection<Discount> findProductDiscounts(Long productId);

}