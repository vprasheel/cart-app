package com.bullish.exercise.bullishcart.repositories;

import com.bullish.exercise.bullishcart.entities.Basket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public interface BasketRepository extends CrudRepository<Basket, Long> {

    @Query("SELECT bi FROM Basket bi WHERE bi.userId = ?1 and isCheckedout=0")
    Collection<Basket> findUserBasket(Long userId);

}
