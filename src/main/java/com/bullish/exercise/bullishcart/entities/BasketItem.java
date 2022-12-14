package com.bullish.exercise.bullishcart.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class BasketItem extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long basketId;
    private Long productId;
    private Integer quantity;
    private Double amount;
    private Double discountAmount;

    public BasketItem(Long productId, Long basketId, int quantity){
        this.productId = productId;
        this.basketId = basketId;
        this.quantity = quantity;
    }

}
