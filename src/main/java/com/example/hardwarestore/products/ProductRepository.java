package com.example.hardwarestore.products;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> findAll();

    Optional<Product> findByName(String name);

    @Query("select count(*) from Product p where p.id in (?1)")
    int countMatchingProductIds(Set<Integer> productIds);
}
