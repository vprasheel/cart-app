package com.bullish.exercise.bullishcart;

import com.bullish.exercise.bullishcart.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Isolated
public class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_productCreate() throws Exception{
        String uri = "/product";
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setActive(1);
        product.setAmount(2.0);
        product.setQuantity(100);
        String content = objectMapper.writeValueAsString(product);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product respProduct = objectMapper.readValue(responseContent, Product.class);
        assert(respProduct.getId() != null);
        assert(respProduct.getName().equals(product.getName()));
        assert(respProduct.getDescription().equals(product.getName()));
        assert(respProduct.getAmount().doubleValue() == product.getAmount().doubleValue());
        assert(respProduct.getQuantity().intValue() == product.getQuantity().intValue());
    }

    @Test
    void test_productCreateAndGet() throws Exception{
        String uri = "/product";
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setActive(1);
        product.setAmount(2.0);
        product.setQuantity(100);
        String content = objectMapper.writeValueAsString(product);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product respProduct = objectMapper.readValue(responseContent, Product.class);
        assert(respProduct.getId() != null);
        assert(respProduct.getName().equals(product.getName()));
        assert(respProduct.getDescription().equals(product.getName()));
        assert(respProduct.getAmount().doubleValue() == product.getAmount().doubleValue());
        assert(respProduct.getQuantity().intValue() == product.getQuantity().intValue());

        uri = "/product/"+ respProduct.getId();
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);

        content = mvcResult.getResponse().getContentAsString();
        respProduct = objectMapper.readValue(content, Product.class);
        assert(respProduct.getId() != null);
        assert(respProduct.getName().equals(product.getName()));
        assert(respProduct.getDescription().equals(product.getName()));
        assert(respProduct.getAmount().doubleValue() == product.getAmount().doubleValue());
        assert(respProduct.getQuantity().intValue() == product.getQuantity().intValue());
    }

    @Test
    void test_productCreateAndGet1() throws Exception{
        String uri = "/product";
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product");
        product.setActive(1);
        product.setAmount(2.0);
        product.setQuantity(100);
        String content = objectMapper.writeValueAsString(product);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Product respProduct = objectMapper.readValue(responseContent, Product.class);
        assert(respProduct.getId() != null);
        assert(respProduct.getName().equals(product.getName()));
        assert(respProduct.getDescription().equals(product.getName()));
        assert(respProduct.getAmount().doubleValue() == product.getAmount().doubleValue());
        assert(respProduct.getQuantity().intValue() == product.getQuantity().intValue());

        uri = "/product/5";
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (404 == status);

    }

}
