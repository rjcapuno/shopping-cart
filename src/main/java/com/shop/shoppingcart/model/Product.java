package com.shop.shoppingcart.model;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    @Column(name = "count_remaining")
    private Integer countRemaining;

    public Product() {
    }

    public Product(Long id, String name, Double price, Integer countRemaining) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.countRemaining = countRemaining;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCountRemaining() {
        return countRemaining;
    }

    public void setCountRemaining(Integer countRemaining) {
        this.countRemaining = countRemaining;
    }
}
