package com.shop.shoppingcart.service;

import com.shop.shoppingcart.exception.CartException;
import com.shop.shoppingcart.model.Product;
import com.shop.shoppingcart.repository.ProductRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRespository productRespository;

    @InjectMocks
    private ProductServiceImpl productService = new ProductServiceImpl();

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(
                1L, "Pencil", 7.50, 5
        );
    }

    @Test
    public void getProductByAvailabilityTest() {
        //Test non-existing product
        Exception exception = assertThrows(CartException.class, () -> {
            productService.getProductByAvailability(999L, 10);
        });
        assertEquals("Product does not exist", exception.getMessage());

        //Test product stock not enough
        when(productRespository.findById(1l)).thenReturn(product);
        exception = assertThrows(CartException.class, () -> {
            productService.getProductByAvailability(product.getId(), 10);
        });
        assertEquals("Stocks are not enough to meet the desired quantity", exception.getMessage());

        //Test out of stock product
        product.setCountRemaining(0);
        exception = assertThrows(CartException.class, () -> {
            productService.getProductByAvailability(product.getId(), 10);
        });
        assertEquals("Product is out of stock", exception.getMessage());
    }
}
