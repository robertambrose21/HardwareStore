package com.example.hardwarestore.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductAdminControllerTest {

    @Mock
    private ProductService productService;

    private ProductAdminController productAdminController;

    @BeforeEach
    public void setUp() {
        productAdminController = new ProductAdminController(productService);
    }

    @Test
    public void addProductReturnsABadRequestIfAnExceptionIsThrown() {
        Product product = new Product();

        when(productService.addProduct(eq(product))).thenThrow(new ProductServiceException("Bad product"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> productAdminController.addProduct(product));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void updateProductReturnsNotFoundIfAnExceptionIsThrown() {
        Product product = new Product();
        int productId = 1;
        product.setId(productId);

        when(productService.updateProduct(eq(productId), eq(product))).thenThrow(new ProductServiceException("Bad product"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> productAdminController.updateProduct(productId, product));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    public void deleteProductReturnsNotFoundIfAnExceptionIsThrown() {
        int productId = 1;

        when(productService.deleteProduct(eq(productId))).thenThrow(new ProductServiceException("Bad product"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> productAdminController.deleteProduct(productId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

}
