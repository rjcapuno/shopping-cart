package com.shop.shoppingcart.controller;

import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(3L, "pen", 7.50, 5);
    }

    @Test
    public void getAllEmptyTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shop/products");

        when(productService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findAll();
    }

    @Test
    public void getAllTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shop/products");

        when(productService.findAll()).thenReturn(Arrays.asList(product));
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].countRemaining").value(product.getCountRemaining()));

        verify(productService, times(1)).findAll();
    }
}
