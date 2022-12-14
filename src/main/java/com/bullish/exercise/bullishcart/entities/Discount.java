package com.bullish.exercise.bullishcart.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Discount extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private static final long serialVersionUID = 6050546704199473367L;
    private Long productId;
    @Basic
    @Temporal(TemporalType.DATE)
    private Date dealStartDate;
    @Basic
    @Temporal(TemporalType.DATE)
    private Date dealEndDate;
    private String description;
    private Double discountValue;
    private String discountRule;
    private Integer active;

}
