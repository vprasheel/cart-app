package com.bullish.exercise.bullishcart.service;

import com.bullish.exercise.bullishcart.exception.ProductNotFoundException;
import com.bullish.exercise.bullishcart.entities.Product;
import com.bullish.exercise.bullishcart.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product){
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Product getProduct(Long productId){
        Optional<Product> productOptional =  productRepository.findById(productId);
        if(productOptional.isPresent())
            return productOptional.get();
        return null;
    }

    public Product updateProduct(Long productId, Product product) throws ProductNotFoundException {
        Optional<Product> savedProductObj = productRepository.findById(productId);
        if(!savedProductObj.isPresent())
            throw new ProductNotFoundException();
        Product savedProduct = savedProductObj.get();
        BeanUtils.copyProperties(product, savedProduct);

        Product updatedProduct = productRepository.save(savedProduct);
        return updatedProduct;
    }

    public Product deleteProduct(Long productId) throws ProductNotFoundException {
        Optional<Product> orgProductObj = productRepository.findById(productId);
        if(!orgProductObj.isPresent())
            throw new ProductNotFoundException();
        Product orgProduct = orgProductObj.get();
        orgProduct.setActive(0);
        Product product = productRepository.save(orgProduct);
        return product;
    }

}
