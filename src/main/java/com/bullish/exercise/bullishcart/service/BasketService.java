package com.bullish.exercise.bullishcart.service;

import com.bullish.exercise.bullishcart.entities.Basket;
import com.bullish.exercise.bullishcart.repositories.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class BasketService {
    @Autowired
    BasketRepository basketRepository;

    public Basket getBasketForUser(Long userId){
        Collection<Basket> baskets = basketRepository.findUserBasket(userId);
        return baskets.iterator().next();
    }

    public Basket getBasketById(Long basketId){
        Optional<Basket> optionalBasket = basketRepository.findById(basketId);
        if(optionalBasket.isPresent())
            return optionalBasket.get();
        return null;

    }

    public Basket createBasket(Long userId){
        Basket basket = new Basket(userId);
        return basketRepository.save(basket);
    }

}
