package com.shop.shoppingcart.repository;

import com.shop.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRespository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    Product findById(long id);
}
