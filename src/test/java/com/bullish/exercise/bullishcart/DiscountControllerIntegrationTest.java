package com.bullish.exercise.bullishcart;

import com.bullish.exercise.bullishcart.entities.Discount;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Isolated
public class DiscountControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void test_DiscountCreate() throws Exception{
        String uri = "/discount";
        Discount discount = new Discount();
        discount.setProductId(1L);
        discount.setDescription("20 % discount");
        discount.setDiscountValue(20.0);
        discount.setActive(1);
        LocalDate lDate = LocalDate.now();
        Date startDate = Date.from(lDate.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lDate.plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        discount.setDealStartDate(startDate);
        discount.setDealEndDate(endDate);
        discount.setDiscountRule("PERCENT_DISCOUNT");
        String content = objectMapper.writeValueAsString(discount);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Discount respDiscount = objectMapper.readValue(responseContent, Discount.class);
        assert(respDiscount.getId() != null);
        assert(respDiscount.getDiscountRule().equals(discount.getDiscountRule()));
        assert(respDiscount.getDescription().equals(discount.getDescription()));
        assert(respDiscount.getDiscountValue().doubleValue() == discount.getDiscountValue().doubleValue());
        assert(respDiscount.getDealStartDate().getTime() == discount.getDealStartDate().getTime());
    }

    @Test
    void test_DiscountCreateAndUpdate() throws Exception{
        String uri = "/discount";
        Discount discount = new Discount();
        discount.setProductId(1L);
        discount.setDescription("20 % discount");
        discount.setDiscountValue(20.0);
        discount.setActive(1);
        LocalDate lDate = LocalDate.now();
        Date startDate = Date.from(lDate.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lDate.plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        discount.setDealStartDate(startDate);
        discount.setDealEndDate(endDate);
        discount.setDiscountRule("PERCENT_DISCOUNT");
        String content = objectMapper.writeValueAsString(discount);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Discount respDiscount = objectMapper.readValue(responseContent, Discount.class);
        assert(respDiscount.getId() != null);
        assert(respDiscount.getDiscountRule().equals(discount.getDiscountRule()));
        assert(respDiscount.getDescription().equals(discount.getDescription()));
        assert(respDiscount.getDiscountValue().doubleValue() == discount.getDiscountValue().doubleValue());
        assert(respDiscount.getDealStartDate().getTime() == discount.getDealStartDate().getTime());

        uri = "/discount/"+respDiscount.getId();
        respDiscount.setDiscountValue(15.0);
        content = objectMapper.writeValueAsString(respDiscount);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (200 == status);
        responseContent = mvcResult.getResponse().getContentAsString();
        respDiscount = objectMapper.readValue(responseContent, Discount.class);
        assert(respDiscount.getId() != null);
        assert(respDiscount.getDiscountRule().equals(discount.getDiscountRule()));
        assert(respDiscount.getDescription().equals(discount.getDescription()));
        assert(respDiscount.getDiscountValue().doubleValue() == 15.0d);
        assert(respDiscount.getDealStartDate().getTime() == discount.getDealStartDate().getTime());

    }

    @Test
    void test_DiscountCreateAndUpdate2() throws Exception{
        String uri = "/discount";
        Discount discount = new Discount();
        discount.setProductId(1L);
        discount.setDescription("20 % discount");
        discount.setDiscountValue(20.0);
        discount.setActive(1);
        LocalDate lDate = LocalDate.now();
        Date startDate = Date.from(lDate.minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(lDate.plusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        discount.setDealStartDate(startDate);
        discount.setDealEndDate(endDate);
        discount.setDiscountRule("PERCENT_DISCOUNT");
        String content = objectMapper.writeValueAsString(discount);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assert (201 == status);

        String responseContent = mvcResult.getResponse().getContentAsString();
        Discount respDiscount = objectMapper.readValue(responseContent, Discount.class);
        assert(respDiscount.getId() != null);
        assert(respDiscount.getDiscountRule().equals(discount.getDiscountRule()));
        assert(respDiscount.getDescription().equals(discount.getDescription()));
        assert(respDiscount.getDiscountValue().doubleValue() == discount.getDiscountValue().doubleValue());
        assert(respDiscount.getDealStartDate().getTime() == discount.getDealStartDate().getTime());

        uri = "/discount/7";
        respDiscount.setDiscountValue(15.0);
        content = objectMapper.writeValueAsString(respDiscount);
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri).content(content).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assert (404 == status);

    }



}
