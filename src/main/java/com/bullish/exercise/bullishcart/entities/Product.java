package com.bullish.exercise.bullishcart.entities;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Product extends AbstractPersistable<Long> {
    private static final long serialVersionUID = -7826398229925703741L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Double amount;
    private Integer active;
    private Integer quantity;

    public Product(Long id){
        this.setId(id);
    }

}
