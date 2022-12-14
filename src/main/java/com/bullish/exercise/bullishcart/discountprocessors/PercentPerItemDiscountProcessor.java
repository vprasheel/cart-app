package com.bullish.exercise.bullishcart.discountprocessors;

import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Discount;

public class PercentPerItemDiscountProcessor implements DiscountProcessor{
    @Override
    public void applyDiscount(BasketItem basketItem, Discount discount) {
        Double purchasePrice = basketItem.getAmount() * basketItem.getQuantity();
        Double discountAmount = (discount.getDiscountValue() * purchasePrice) / 100.0;
        basketItem.setAmount(purchasePrice - discountAmount);
        //basketItem.getDiscounts().add(discount);
        basketItem.setDiscountAmount(discountAmount);
    }
}
