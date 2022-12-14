package com.bullish.exercise.bullishcart.repositories;

import com.bullish.exercise.bullishcart.entities.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> { }
