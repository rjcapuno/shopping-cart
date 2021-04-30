package com.shop.shoppingcart.controller;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Cart;
import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.model.User;
import com.shop.shoppingcart.request.CartRequest;
import com.shop.shoppingcart.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {
    @MockBean
    private CartService cartService;

    @Autowired
    private MockMvc mockMvc;

    private Cart cart;
    private Product product;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(
                3L, "RJ", "Laguna", "rjcapuno@gmail.com"
                , "$2y$12$ZB6hoUg6pjBjl/nhe5Ffserhy37BzHGZuzFO5YxDsj19pn2yx/cNi"
                , new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
        );

        product = new Product(3L, "pen", 7.50, 5);
        List<Product> products = new ArrayList<>();
        products.add(product);

        cart = new Cart(
                3L, user, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
                , products, 27.50
        );
    }

    @Test
    public void getCartByUserIdTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/shop/users/3/cart");
        when(cartService.getCartByUserId(any())).thenReturn(cart);
        testResponseDataSuccess(requestBuilder);
    }

    @Test
    public void createCartFailTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shop/users/3/cart")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"productId\": 1, \"quantity\": 1 }");

        String messsage = "User does not exist";
        doThrow(new CartException(messsage)).when(cartService).createCart(any(CartRequest.class), anyLong());
        testResponseDataFail(requestBuilder, messsage);

        verify(cartService, times(1)).createCart(any(CartRequest.class), anyLong());
        verify(cartService, times(0)).getCartByUserId(any());
    }

    @Test
    public void createCartSuccessTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/shop/users/3/cart")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"productId\": 1, \"quantity\": 1 }");

        doNothing().when(cartService).createCart(any(CartRequest.class), anyInt());
        when(cartService.getCartByUserId(3l)).thenReturn(cart);
        testResponseDataSuccess(requestBuilder);

        verify(cartService, times(1)).createCart(any(CartRequest.class), anyLong());
        verify(cartService, times(1)).getCartByUserId(any());
    }

    @Test
    public void updateCartFailTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/shop/users/3/cart")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"productId\": 1, \"quantity\": 1 }");

        String messsage = "No cart available";
        doThrow(new CartException(messsage)).when(cartService).updateCart(any(CartRequest.class), anyLong());
        testResponseDataFail(requestBuilder, messsage);

        verify(cartService, times(1)).updateCart(any(CartRequest.class), anyLong());
        verify(cartService, times(0)).getCartByUserId(any());

    }

    @Test
    public void updateCartSuccessTest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/shop/users/3/cart")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"productId\": 1, \"quantity\": 1 }");

        doNothing().when(cartService).updateCart(any(CartRequest.class), anyInt());
        when(cartService.getCartByUserId(3l)).thenReturn(cart);
        testResponseDataSuccess(requestBuilder);

        verify(cartService, times(1)).updateCart(any(CartRequest.class), anyLong());
        verify(cartService, times(1)).getCartByUserId(any());

    }

    private void testResponseDataFail(RequestBuilder requestBuilder, String message) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(message));
    }

    private void testResponseDataSuccess(RequestBuilder requestBuilder) throws Exception {
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(cart.getId()))
                .andExpect(jsonPath("$.data.userId").value(cart.getUser().getId()))
                .andExpect(jsonPath("$.data.createdAt").value(cart.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data.updatedAt").value(cart.getUpdatedAt().toString()))
                .andExpect(jsonPath("$.data.totalPrice").value(cart.getTotalPrice()))
                .andExpect(jsonPath("$.data.products[0].id").value(product.getId()))
                .andExpect(jsonPath("$.data.products[0].name").value(product.getName()))
                .andExpect(jsonPath("$.data.products[0].price").value(product.getPrice()));
    }
}
