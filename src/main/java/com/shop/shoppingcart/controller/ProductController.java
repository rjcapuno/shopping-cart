package com.shop.shoppingcart.controller;

import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping
    public List<Product> getAll() {
        return productService.findAll();
    }
}
