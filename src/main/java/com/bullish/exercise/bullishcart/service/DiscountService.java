package com.bullish.exercise.bullishcart.service;

import com.bullish.exercise.bullishcart.exception.DiscountNotFoundException;
import com.bullish.exercise.bullishcart.entities.Discount;
import com.bullish.exercise.bullishcart.repositories.DiscountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    DiscountRepository discountRepository;

    public Discount createDiscount(Discount discount){
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long discountId, Discount discount) throws DiscountNotFoundException {
        Optional<Discount> savedDiscountObj = discountRepository.findById(discountId);
        if(!savedDiscountObj.isPresent())
            throw new DiscountNotFoundException();
        Discount savedDiscount = savedDiscountObj.get();
        BeanUtils.copyProperties(discount, savedDiscount);

        Discount updatedDiscount = discountRepository.save(savedDiscount);
        return updatedDiscount;
    }


}
