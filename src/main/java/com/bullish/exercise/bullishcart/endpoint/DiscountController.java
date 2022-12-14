package com.bullish.exercise.bullishcart.endpoint;

import com.bullish.exercise.bullishcart.entities.Discount;
import com.bullish.exercise.bullishcart.exception.DiscountNotFoundException;
import com.bullish.exercise.bullishcart.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Discount create(@RequestBody Discount discount){
        return discountService.createDiscount(discount);
    }

    @PutMapping("/{discountId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long discountId, @RequestBody Discount discount){
        Discount savedDiscount = null;
        try{
            savedDiscount = discountService.updateDiscount(discountId, discount);
        }catch (DiscountNotFoundException dnfe){
            return new ResponseEntity<>(savedDiscount, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(savedDiscount, HttpStatus.OK);
    }
}
