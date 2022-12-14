package com.bullish.exercise.bullishcart.requests;

import lombok.Data;

@Data
public class AddToBasketRequest {
    private Long productId;
    private Integer quantity;

}
