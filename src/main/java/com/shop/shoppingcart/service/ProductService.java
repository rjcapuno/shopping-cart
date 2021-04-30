package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(long id);
    Product getProductByAvailability(long productId, int quantity) throws CartException;
}
