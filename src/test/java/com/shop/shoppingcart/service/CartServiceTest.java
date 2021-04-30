package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Cart;
import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.model.User;
import com.shop.shoppingcart.repository.CartRepository;
import com.shop.shoppingcart.request.CartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService = new CartServiceImpl();

    private User user;
    private Product product;
    private Cart cart;
    private CartRequest request;

    @BeforeEach
    public void setUp() {
        user = new User(
                1L, "RJ", "Laguna", "rjcapuno@gmail.com"
                , "$2y$12$ZB6hoUg6pjBjl/nhe5Ffserhy37BzHGZuzFO5YxDsj19pn2yx/cNi"
                , new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
        );

        product = new Product(
                1L, "Pencil", 7.50, 5
        );

        List<Product> products = new ArrayList<>();
        products.add(product);
        cart = new Cart(
                1L, user, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())
                , products, 7.50
        );

        request = new CartRequest(
                product.getId(), 1
        );

    }

    @Test
    public void getCartByUserIdTest() throws CartException {
        //Test non-existing cart
        Exception exception = assertThrows(CartException.class, () -> {
            cartService.getCartByUserId(999L);
        });
        assertEquals("Cart does not exist", exception.getMessage());


        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);
        Cart actualCart = cartService.getCartByUserId(user.getId());
        assertNotNull(actualCart);
        assertEquals(user.getId(), actualCart.getUser().getId());
    }

    @Test
    public void validateRequestTest() {
        CartRequest cartRequestInvalidQuantity = new CartRequest(
            1L, 0
        );

        Exception exception = assertThrows(CartException.class, () -> {
            cartService.validateRequest(cartRequestInvalidQuantity);
        });
        //Test invalid quantity
        assertNotEquals("Cart does not exist", exception.getMessage());
        assertEquals("Invalid quantity", exception.getMessage());

        CartRequest cartRequestInvalidProductId = new CartRequest(
                null, 1
        );

        exception = assertThrows(CartException.class, () -> {
            cartService.validateRequest(cartRequestInvalidProductId);
        });
        //Test invalid Product ID
        assertNotEquals("Cart does not exist", exception.getMessage());
        assertEquals("productId cannot be empty.", exception.getMessage());
    }

    @Test
    public void createCartTest() throws CartException {
        //Test non-existing user
        Exception exception = assertThrows(CartException.class, () -> {
            cartService.createCart(request, 1L);
        });
        assertEquals("User does not exist", exception.getMessage());

        //Test no. of invokes
        when(userService.findById(user.getId())).thenReturn(user);
        when(productService.getProductByAvailability(product.getId(), 1)).thenReturn(product);
        cartService.createCart(request, user.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));

    }

    @Test
    public void updateCartTest() throws CartException {
        //Test non-existing Cart
        Exception exception = assertThrows(CartException.class, () -> {
            cartService.updateCart(request, 1L);
        });
        assertEquals("No cart available", exception.getMessage());

        //Test no. of invokes
        when(cartRepository.findByUserId(user.getId())).thenReturn(cart);
        when(userService.findById(user.getId())).thenReturn(user);
        when(productService.getProductByAvailability(product.getId(), 1)).thenReturn(product);
        cartService.updateCart(request, user.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void recalculatePriceTest() {
        Double currentTotalPrice = 100.00, productPrice = 25.00;
        int productQuantity = 2, productCountRemaining = 5;

        //Test recalculation of adding products
        Double recalculatedPrice =
                cartService.recalculatePrice(
                        currentTotalPrice,
                        productPrice,
                        productQuantity,
                        productCountRemaining
                );
        assertEquals(new Double(150), recalculatedPrice);

        //Test recalculation of removing products
        productQuantity = -2;
        recalculatedPrice =
                cartService.recalculatePrice(
                        currentTotalPrice,
                        productPrice,
                        productQuantity,
                        productCountRemaining
                );
        assertEquals(new Double(50), recalculatedPrice);

        //Test recalculation of removing products exceeding the count remaining
        productCountRemaining = 1;
        recalculatedPrice =
                cartService.recalculatePrice(
                        currentTotalPrice,
                        productPrice,
                        productQuantity,
                        productCountRemaining
                );
        assertEquals(new Double(75), recalculatedPrice);
    }
}
