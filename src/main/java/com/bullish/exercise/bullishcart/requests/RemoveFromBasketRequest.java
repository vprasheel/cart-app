package com.bullish.exercise.bullishcart.requests;

import lombok.Data;

@Data
public class RemoveFromBasketRequest {

    private Long basketId;
    private Long productId;
    private Integer quantity;

}
