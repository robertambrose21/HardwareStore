package com.example.hardwarestore.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        if(!productRepository.findByName(product.getName()).isEmpty()) {
            throw new ProductServiceException("Product with name " + product.getName() + "already exists");
        }

        return productRepository.save(product);
    }

    public Product updateProduct(int productId, Product product) {
        Optional<Product> original = productRepository.findById(productId);

        if(original.isEmpty()) {
            throw new ProductServiceException("Product with id " + productId + "does not exist");
        }

        product.setId(original.get().getId());

        return productRepository.save(product);
    }

    public Product deleteProduct(int productId) {
        Optional<Product> product = productRepository.findById(productId);

        if(product.isEmpty()) {
            throw new ProductServiceException("Product with id " + productId + "does not exist");
        }

        productRepository.delete(product.get());

        return product.get();
    }

}
