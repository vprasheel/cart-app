package com.bullish.exercise.bullishcart.requests;

import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Discount;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class BasketReviewResponse {

    private Collection<BasketItem> products;

    private Collection<Discount> discounts;
    private Double totalAmount;

}
