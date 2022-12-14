package com.bullish.exercise.bullishcart.service;

import com.bullish.exercise.bullishcart.discountprocessors.DiscountProcessor;
import com.bullish.exercise.bullishcart.discountprocessors.DiscountProcessorFactory;
import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Discount;
import com.bullish.exercise.bullishcart.entities.Product;
import com.bullish.exercise.bullishcart.exception.InvalidProductQuantityInBasketException;
import com.bullish.exercise.bullishcart.exception.InvalidUserBasketException;
import com.bullish.exercise.bullishcart.exception.NoProductInBasketException;
import com.bullish.exercise.bullishcart.repositories.BasketItemRepository;
import com.bullish.exercise.bullishcart.repositories.DiscountRepository;
import com.bullish.exercise.bullishcart.repositories.ProductRepository;
import com.bullish.exercise.bullishcart.requests.BasketReviewResponse;
import com.bullish.exercise.bullishcart.util.DiscountRuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class BasketItemService {

    @Autowired
    BasketItemRepository basketItemRepository;
    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    ProductRepository productRepository;
    public BasketItem addToBasket(Long basketId, Long userId, Long productId, int quantity) throws InvalidUserBasketException {
        BasketItem bi = null;
        if(basketId != null) {
            Collection<BasketItem> basketItems = basketItemRepository.findUserBasketForProduct(basketId, productId);
            BasketItem basketItem = null;
            Product product = productRepository.findById(productId).get();
            if (basketItems.size() > 1)
                throw new InvalidUserBasketException();
            else if (basketItems.size() == 0) {
                basketItem = new BasketItem(productId, basketId, quantity);
                basketItem.setAmount(basketItem.getQuantity() * product.getAmount());
            } else {
                basketItem = basketItems.iterator().next();
                basketItem.setQuantity(basketItem.getQuantity() + quantity);
                basketItem.setAmount(basketItem.getQuantity() * product.getAmount());
            }
            bi = basketItemRepository.save(basketItem);
        }
        return bi;
    }

    public BasketItem removeFromBasket(Long basketId, Long userId, Long productId, int quantity)
            throws InvalidUserBasketException, NoProductInBasketException, InvalidProductQuantityInBasketException {
        Collection<BasketItem> basketItems = basketItemRepository.findUserBasketForProduct(basketId, productId);
        BasketItem basketItem = null;
        Product product = productRepository.findById(productId).get();
        if (basketItems.size() > 1)
            throw new InvalidUserBasketException();
        else if (basketItems.size() <= 0){
            throw new NoProductInBasketException();
        }else{
            basketItem = basketItems.iterator().next();
            if(quantity > basketItem.getQuantity())
                throw new InvalidProductQuantityInBasketException();
            if(basketItem.getQuantity() > quantity){
                basketItem.setQuantity(basketItem.getQuantity() - quantity);
                basketItem.setAmount(basketItem.getQuantity() * product.getAmount());
                return basketItemRepository.save(basketItem);
            }
            else if(basketItem.getQuantity() == quantity){
                basketItemRepository.delete(basketItem);
            }
        }
        return null;
    }

    public BasketReviewResponse reviewBasket( Long basketId) {
        Collection<BasketItem> basketItems = basketItemRepository.findBasketItems(basketId);
        BasketReviewResponse response = new BasketReviewResponse();
        Double finalAmount = 0.0;
        for (BasketItem basketItem: basketItems) {
            Collection<Discount> discounts = discountRepository.findProductDiscounts(basketItem.getProductId());
            for(Discount discount: discounts){
                DiscountProcessor discountProcessor = DiscountProcessorFactory.getDiscountProcessor(DiscountRuleEnum.valueOf(discount.getDiscountRule()));
                discountProcessor.applyDiscount(basketItem, discount);
                if(response.getDiscounts() == null){
                    List<Discount> discountList = new ArrayList<>();
                    discountList.add(discount);
                    response.setDiscounts(discountList);
                }else{
                    response.getDiscounts().add(discount);
                }

            }
            finalAmount += basketItem.getAmount();
            if(response.getProducts() == null){
                List products = new ArrayList();
                products.add(basketItem);
                response.setProducts(products);
            }else{
                response.getProducts().add(basketItem);
            }
        }
        response.setTotalAmount(finalAmount);
        return response;
    }
}
