package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Cart;
import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.model.User;
import com.shop.shoppingcart.repository.CartRepository;
import com.shop.shoppingcart.request.CartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public Cart getCartByUserId(Long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null) {
            throw new CartException("Cart does not exist");
        }

        return cart;
    }

    @Override
    public void validateRequest(CartRequest request) throws CartException {
        if(request.getProductId() == null) {
            throw new CartException("productId cannot be empty.");
        }

        if(request.getQuantity() == 0) {
            throw new CartException("Invalid quantity");
        }
    }

    @Override
    @Transactional
    public void createCart(CartRequest request, long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart != null) {
            throw new CartException("Cart already exists");
        }

        User user = userService.findById(userId);
        if(user == null) {
            throw new CartException("User does not exist");
        }

        Product product = productService.getProductByAvailability(request.getProductId(), request.getQuantity());
        List<Product> products = new ArrayList<>();
        products.addAll( populateProductByQuantity(product, request.getQuantity()) );

        cart = new Cart();
        cart.setUser(user);
        cart.setProducts(products);
        cart.setTotalPrice(product.getPrice() * request.getQuantity());
        cart.setCreatedAt(new Date(System.currentTimeMillis()));
        cart.setUpdatedAt(new Date(System.currentTimeMillis()));

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void updateCart(CartRequest request, long userId) throws CartException {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null) {
            throw new CartException("No cart available");
        }

        List<Product> products = cart.getProducts();
        Product product = null;
        if(request.getQuantity() > 0) {
            product = productService.getProductByAvailability(request.getProductId(), request.getQuantity());
            List<Long> ids = cart.getProducts().stream()
                    .filter(p -> p.getId() == request.getProductId()).map(Product::getId).collect(Collectors.toList());
            if(ids.size() + request.getQuantity() > product.getCountRemaining()){
                throw new CartException("Stocks are not enough to meet the desired quantity");
            }

            products.addAll( populateProductByQuantity(product, request.getQuantity()) );
        } else {
            product = cart.getProducts().stream()
                    .filter(p -> p.getId() == request.getProductId()).findFirst().orElse(null);
            if(product == null) {
                throw new CartException("Product/s does not exist in your cart");
            }

            for(int i=0; i<Math.abs(request.getQuantity()); i++) {
                products.remove(product);
            }
        }
        cart.setTotalPrice(recalculatePrice(cart.getTotalPrice(), product.getPrice(),
                request.getQuantity(), product.getCountRemaining()));
        cart.setProducts(products);
        cart.setUpdatedAt(new Date(System.currentTimeMillis()));

        cartRepository.save(cart);
    }

    public Double recalculatePrice(Double totalPrice, Double productPrice,
                                   int quantity, int countRemaining) {
        int multiplier = quantity < 0 ? -1 : 1;
        quantity = Math.abs(quantity);
        quantity = quantity > countRemaining ? countRemaining : quantity;

        return totalPrice + (quantity * multiplier * productPrice);
    }

    private List<Product> populateProductByQuantity(Product product, int quantity) {
        List<Product> products = new ArrayList<>();
        for(int i=0; i<quantity; i++) {
            products.add(product);
        }

        return products;
    }

}
