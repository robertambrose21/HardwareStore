package com.example.hardwarestore.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    public void getAllProductsGetsAllProducts() {
        productService.getAllProducts();

        verify(productRepository).findAll();
    }

    @Test
    public void addProductThrowsExceptionIfProductAlreadyExists() {
        Product product = new Product();
        String productName = "Sledgehammer";
        product.setName(productName);

        when(productRepository.findByName(productName)).thenReturn(Optional.of(new Product()));

        assertThrows(ProductServiceException.class, () -> productService.addProduct(product));
    }

    @Test
    public void addProductAddsAProduct() {
        Product product = new Product();
        String productName = "Sledgehammer";
        product.setName(productName);

        when(productRepository.findByName(productName)).thenReturn(Optional.empty());

        productService.addProduct(product);

        verify(productRepository).save(product);
    }

    @Test
    public void updateProductThrowsExceptionIfProductDoesNotExist() {
        Product product = new Product();
        int productId = 1;
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductServiceException.class, () -> productService.updateProduct(productId, product));
    }

    @Test
    public void updateProductUpdatesAProduct() {
        Product product = new Product();
        int productId = 1;
        product.setId(productId);
        product.setName("Sledgehammer");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.updateProduct(productId, product);

        verify(productRepository).save(product);
    }

    @Test
    public void deleteProductThrowsAnExceptionIfProductDoesNotExist() {
        int productId = 1;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductServiceException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    public void deleteProductDeletesAProduct() {
        Product product = new Product();
        int productId = 1;
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        productService.deleteProduct(productId);

        verify(productRepository).delete(product);
    }

}
