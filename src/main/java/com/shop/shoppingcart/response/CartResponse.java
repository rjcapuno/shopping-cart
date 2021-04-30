package com.shop.shoppingcart.response;

import com.shop.shoppingcart.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CartResponse {

    private Long id;
    private Long userId;
    private Date createdAt;
    private Date updatedAt;
    private Double totalPrice;
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ProductResponse> getProducts() {
        return products.stream()
                .map(product ->
                        new ProductResponse(product.getId(), product .getName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
