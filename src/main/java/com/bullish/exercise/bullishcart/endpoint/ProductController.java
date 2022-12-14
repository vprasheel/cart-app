package com.bullish.exercise.bullishcart.endpoint;

import com.bullish.exercise.bullishcart.exception.ProductNotFoundException;
import com.bullish.exercise.bullishcart.entities.Product;
import com.bullish.exercise.bullishcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity getProduct(@PathVariable Long productId){
        Product prod = productService.getProduct(productId);
        if(prod == null)
            return new ResponseEntity<>(productId, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(prod, HttpStatus.OK);
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product){
        return productService.createProduct(product);
    }

    @PutMapping(value="/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable Long productId, @RequestBody Product product){
        Product savedProduct = null;
        try{
            savedProduct = productService.updateProduct(productId, product);
        }catch (ProductNotFoundException pnfe){
            return new ResponseEntity<>(savedProduct, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(savedProduct, HttpStatus.OK);
    }

    @DeleteMapping(value="/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@PathVariable Long productId){
        try{
            productService.deleteProduct(productId);
        }catch (ProductNotFoundException pnfe){
            return new ResponseEntity<>(productId, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productId, HttpStatus.OK);
    }

}
