package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Cart;
import com.shop.shoppingcart.request.CartRequest;

public interface CartService {
    Cart getCartByUserId(Long userId) throws CartException;
    void validateRequest(CartRequest request) throws CartException;
    void createCart(CartRequest request, long userId) throws CartException;
    void updateCart(CartRequest request, long userId) throws CartException;
}
