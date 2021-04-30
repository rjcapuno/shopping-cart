package com.shop.shoppingcart.controller;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Cart;
import com.shop.shoppingcart.request.CartRequest;
import com.shop.shoppingcart.response.ApiResponse;
import com.shop.shoppingcart.response.CartResponse;
import com.shop.shoppingcart.service.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/users/{userId}/cart")
    public ApiResponse getCartByUserId(@PathVariable Long userId) {
        Cart cart = null;
        ApiResponse response = new ApiResponse();

        try {
            cart = cartService.getCartByUserId(userId);
            response.setSuccess(true);
            response.setData(modelMapper.map(cart, CartResponse.class));
        } catch (CartException e) {
            LOG.error("Unable to retrieve cart: " + e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @PostMapping("/users/{userId}/cart")
    public ApiResponse createCart(@RequestBody CartRequest request, @PathVariable Long userId) {
        ApiResponse response = new ApiResponse();
        try {
            cartService.validateRequest(request);
            cartService.createCart(request, userId);
            response.setSuccess(true);
            Cart cart = cartService.getCartByUserId(userId);
            response.setData(modelMapper.map(cart, CartResponse.class));
        } catch (CartException e) {
            LOG.error("Unable to create cart: " + e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @PutMapping("/users/{userId}/cart")
    public ApiResponse upsertCart(@RequestBody CartRequest request, @PathVariable Long userId) {
        ApiResponse response = new ApiResponse();
        try {
            cartService.validateRequest(request);
            //Apply cart updates;
            //quantity is 0 : no changes,
            //quantity is positive : add to cart,
            //quantity is negative : remove from cart
            cartService.updateCart(request, userId);
            response.setSuccess(true);
            Cart cart = cartService.getCartByUserId(userId);
            response.setData(modelMapper.map(cart, CartResponse.class));
        } catch (CartException e) {
            LOG.error("Unable to update cart: " + e.getMessage());
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }

        return response;
    }

}
