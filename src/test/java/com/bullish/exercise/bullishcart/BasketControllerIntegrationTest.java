package com.bullish.exercise.bullishcart;

import com.bullish.exercise.bullishcart.entities.Basket;
import com.bullish.exercise.bullishcart.entities.BasketItem;
import com.bullish.exercise.bullishcart.entities.Discount;
import com.bullish.exercise.bullishcart.entities.Product;
import com.bullish.exercise.bullishcart.repositories.DiscountRepository;
import com.bullish.exercise.bullishcart.repositories.ProductRepository;
import com.bullish.exercise.bullishcart.requests.AddToBasketRequest;
import com.bullish.exercise.bullishcart.requests.BasketReviewResponse;
import com.bullish.exercise.bullishcart.requests.RemoveFromBasketRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Isolated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Isolated
public class BasketControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DiscountRepository discountRepository;

    Product p1 = null;
    Product p2 = null;
    Discount d1 = null;

    @BeforeEach
    public void setup(){
        Product p1 = createProduct(1);
        Product p2 = createProduct(2);
        Discount d1 = createDiscount(1, p1.getId());
    }

    @AfterEach
    public void teardown(){
        deleteProduct();
        deleteDiscount();
    }
    public Product createProduct(int index){
        Product p = new Product();
        p.setName("Test Product " + index);
        p.setDescription("Test Product " + index);
        p.setActive(1);
        p.setAmount(2.0 * index);
        p.setQuantity(100);
        return productRepository.save(p);
    }

    public void deleteProduct(){
        productRepository.deleteAll();

    }

    private void deleteDiscount(){
        discountRepository.deleteAll();
    }

    private Discount createDiscount(int index, Long productId){
        Discount d = new Discount();
        d.setDescription("Test Discount " + index);
        d.setDiscountValue(1.0);
        d.setDiscountRule("DOLLAR_DISCOUNT");
        d.setActive(1);
        LocalDate lDate = LocalDate.now();
        Date startDate = Date.from(lDate.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lDate.plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        d.setDealStartDate(startDate);
        d.setDealEndDate(endDate);
        d.setProductId(productId);
        return discountRepository.save(d);
    }

    @Test
    void test_BasketCreate() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);
    }

    @Test
    void test_AddToBasket() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }
        request.setProductId(p1.getId());
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem.getQuantity().intValue() == request.getQuantity().intValue());

    }

    @Test
    void test_AddToBasketInvalidProduct() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        request.setProductId(312313l);
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (400 == status);
    }

    @Test
    void test_AddToBasketInvalidQuantity() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }
        request.setProductId(p1.getId());
        request.setQuantity(200);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (400 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        assert(responseContent.equals("Requested quantity cannot be more than available quantity"));

    }

    @Test
    void test_Add2ProductsToBasket() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }
        request.setProductId(p1.getId());
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/add";
        request = new AddToBasketRequest();
        if (p2 == null){
            p2 = createProduct(2);
        }
        request.setProductId(p2.getId());
        request.setQuantity(4);
        content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem1 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem1.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem1.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem1.getQuantity().intValue() == request.getQuantity().intValue());

    }

    @Test
    void test_Add2ProductsToBasketRemove1() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }
        request.setProductId(p1.getId());
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/add";
        request = new AddToBasketRequest();
        if(p2 == null)
            p2 = createProduct(2);
        request.setProductId(p2.getId());
        request.setQuantity(4);
        content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem1 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem1.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem1.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem1.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/remove";
        RemoveFromBasketRequest remRequest = new RemoveFromBasketRequest();
        if (p2 == null){
            p2 = createProduct(2);
        }
        remRequest.setProductId(p2.getId());
        remRequest.setQuantity(2);
        content = objectMapper.writeValueAsString(remRequest);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem2 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem2.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem2.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem2.getQuantity().intValue() == 2);

    }

    @Test
    void test_Add2ProductsToBasketRemove1AndReview() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }

        request.setProductId(p1.getId());
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/add";
        request = new AddToBasketRequest();
        if (p2 == null)
            p2 = createProduct(1);
        request.setProductId(p2.getId());
        request.setQuantity(4);
        content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem1 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem1.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem1.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem1.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/remove";
        RemoveFromBasketRequest remRequest = new RemoveFromBasketRequest();
        remRequest.setProductId(p2.getId());
        remRequest.setQuantity(2);
        content = objectMapper.writeValueAsString(remRequest);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem2 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem2.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem2.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem2.getQuantity().intValue() == 2);

        uri = "/basket/"+ basket.getId() + "/review";

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketReviewResponse basketReviewResponse = objectMapper.readValue(responseContent, BasketReviewResponse.class);
        assert(basketReviewResponse.getProducts().size() == 2);
        Collection<BasketItem> products = basketReviewResponse.getProducts();
        Iterator<BasketItem> prodIterator = products.iterator();
        BasketItem bi1 = prodIterator.next();
        assert(bi1.getProductId() == p1.getId());
        assert(bi1.getQuantity() == 2);

        BasketItem bi2 = prodIterator.next();
        assert(bi2.getProductId() == p2.getId());
        assert(bi2.getQuantity() == 2);

        Collection<Discount> discounts =  basketReviewResponse.getDiscounts();
        assert(discounts.size() == 1);
        Discount discount = discounts.iterator().next();
        assert(discount.getProductId() == p1.getId());
    }

    @Test
    void test_ReviewInvalidBasket() throws Exception {
        String uri = "/basket";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        String responseContent = mvcResult.getResponse().getContentAsString();
        Basket basket = objectMapper.readValue(responseContent, Basket.class);
        assert(basket.getUserId() == 1);
        assert(basket.getId() != null);

        uri = "/basket/"+ basket.getId() + "/add";
        AddToBasketRequest request = new AddToBasketRequest();
        if (p1 == null){
            p1 = createProduct(1);
            d1 = createDiscount(1, p1.getId());
        }

        request.setProductId(p1.getId());
        request.setQuantity(2);
        String content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/add";
        request = new AddToBasketRequest();
        if (p2 == null)
            p2 = createProduct(1);
        request.setProductId(p2.getId());
        request.setQuantity(4);
        content = objectMapper.writeValueAsString(request);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem1 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem1.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem1.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem1.getQuantity().intValue() == request.getQuantity().intValue());

        uri = "/basket/"+ basket.getId() + "/remove";
        RemoveFromBasketRequest remRequest = new RemoveFromBasketRequest();
        remRequest.setProductId(p2.getId());
        remRequest.setQuantity(2);
        content = objectMapper.writeValueAsString(remRequest);

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        BasketItem basketItem2 = objectMapper.readValue(responseContent, BasketItem.class);
        assert(basketItem2.getBasketId().longValue() == basket.getId().longValue());
        assert(basketItem2.getProductId().longValue() == request.getProductId().longValue());
        assert(basketItem2.getQuantity().intValue() == 2);

        uri = "/basket/523532542/review";

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).header("user_id", 1).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        status = mvcResult.getResponse().getStatus();
        assert (400 == status);

    }
}
