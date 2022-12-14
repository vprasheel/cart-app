package com.bullish.exercise.bullishcart.repositories;

import com.bullish.exercise.bullishcart.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {}
