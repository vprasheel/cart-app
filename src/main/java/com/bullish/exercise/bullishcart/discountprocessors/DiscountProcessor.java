package com.bullish.exercise.bullishcart.discountprocessors;

import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Discount;

public interface DiscountProcessor {

    public void applyDiscount(BasketItem basketItem, Discount discount);
}
