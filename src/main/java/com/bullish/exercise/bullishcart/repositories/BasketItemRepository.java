package com.bullish.exercise.bullishcart.repositories;

import com.bullish.exercise.bullishcart.entities.BasketItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {

    @Query("SELECT bi FROM BasketItem bi WHERE basketId = ?1 and productId = ?2")
    Collection<BasketItem> findUserBasketForProduct( Long basketId, Long productId);

    @Query("SELECT bi FROM BasketItem bi WHERE basketId = ?1")
    Collection<BasketItem> findBasketItems(Long basketId);

}
