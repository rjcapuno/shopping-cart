package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.repository.ProductRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRespository productRespository;

    public List<Product> findAll() {
        return productRespository.findAll();
    }

    public Product findById(long id) { return productRespository.findById(id); }

    public Product getProductByAvailability(long productId, int quantity) throws CartException {
        Product product = productRespository.findById(productId);
        if(product == null) {
            throw new CartException("Product does not exist");
        }

        if(product.getCountRemaining() <= 0) {
            throw new CartException("Product is out of stock");
        }

        if(product.getCountRemaining() < quantity) {
            throw new CartException("Stocks are not enough to meet the desired quantity");
        }

        return product;
    }
}
