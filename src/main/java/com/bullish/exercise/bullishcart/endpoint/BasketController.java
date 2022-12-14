package com.bullish.exercise.bullishcart.endpoint;

import com.bullish.exercise.bullishcart.entities.Basket;
import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Product;
import com.bullish.exercise.bullishcart.exception.InvalidProductQuantityInBasketException;
import com.bullish.exercise.bullishcart.exception.InvalidUserBasketException;
import com.bullish.exercise.bullishcart.exception.NoProductInBasketException;
import com.bullish.exercise.bullishcart.requests.AddToBasketRequest;
import com.bullish.exercise.bullishcart.requests.BasketReviewResponse;
import com.bullish.exercise.bullishcart.service.BasketItemService;
import com.bullish.exercise.bullishcart.service.BasketService;
import com.bullish.exercise.bullishcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/basket")
public class BasketController {

    @Autowired
    BasketItemService basketItemService;

    @Autowired
    BasketService basketService;
    @Autowired
    ProductService productService;

    @GetMapping(value="")
    public ResponseEntity getBasket(@RequestHeader Map<String, String> headers){
        Long userId = headers.get("USER_ID") != null ? Long.valueOf(headers.get("USER_ID")) : -1;
        if(userId == null || userId == -1)
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        Basket basket = basketService.getBasketForUser(userId);
        return new ResponseEntity<>(basket, HttpStatus.OK);
    }

    @PostMapping(value="")
    public ResponseEntity createBasket(@RequestHeader Map<String, String> headers){
        Long userId = headers.get("user_id") != null ? Long.valueOf(headers.get("user_id")) : -1;
        if(userId == null || userId == -1)
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        Basket basket = basketService.createBasket(userId);
        return new ResponseEntity<>(basket, HttpStatus.OK);
    }

    @PostMapping(value="{basketId}/add")
    public ResponseEntity addToBasket(@RequestBody AddToBasketRequest request, @RequestHeader Map<String, String> headers, @PathVariable Long basketId) throws InvalidUserBasketException{
        Long userId = headers.get("user_id") != null ? Long.valueOf(headers.get("user_id")) : -1;
        Long productId = request.getProductId();
        BasketItem bi = null;
        if(userId == null || userId == -1)
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        Product product = productService.getProduct(productId);
        if(product == null )
            return new ResponseEntity<>("Invalid Product", HttpStatus.BAD_REQUEST);
        if(request.getQuantity() > product.getQuantity())
            return new ResponseEntity<>("Requested quantity cannot be more than available quantity", HttpStatus.BAD_REQUEST);
        try{
            bi = basketItemService.addToBasket(basketId,userId, productId, request.getQuantity());
        }catch(InvalidUserBasketException iube){
            return new ResponseEntity<>("The user have more than one active baskets", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(bi, HttpStatus.OK);
    }

    @PostMapping(value="{basketId}/remove")
    public ResponseEntity removeFromBasket(@RequestBody AddToBasketRequest request, @RequestHeader Map<String, String> headers, @PathVariable Long basketId){
        Long userId = headers.get("user_id") != null ? Long.valueOf(headers.get("user_id")) : -1;
        Long productId = request.getProductId();
        BasketItem bi = null;
        if(userId == null || userId == -1)
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        try{
            bi = basketItemService.removeFromBasket(basketId,userId, productId, request.getQuantity());
        }catch(InvalidUserBasketException iube){
            return new ResponseEntity<>(bi, HttpStatus.NOT_FOUND);
        }catch (NoProductInBasketException npibe){
            return new ResponseEntity<>(bi, HttpStatus.BAD_REQUEST);
        }catch (InvalidProductQuantityInBasketException ipqib){
            return new ResponseEntity<>(bi, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(bi, HttpStatus.OK);
    }

    @PostMapping(value="{basketId}/review")
    public ResponseEntity reviewBasket( @RequestHeader Map<String, String> headers, @PathVariable Long basketId) throws InvalidUserBasketException{
        Long userId = headers.get("user_id") != null ? Long.valueOf(headers.get("user_id")) : -1;
        BasketReviewResponse bi = null;
        if(userId == null || userId == -1)
            return new ResponseEntity<>("Invalid User", HttpStatus.BAD_REQUEST);
        Basket basket = basketService.getBasketById(basketId);
        if (basket == null)
            return new ResponseEntity<>("Invalid Basket", HttpStatus.BAD_REQUEST);
        bi = basketItemService.reviewBasket(basketId);
        return new ResponseEntity<>(bi, HttpStatus.OK);
    }
}
