package com.example.hardwarestore.products;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
//@NamedQuery(
//        name = Product.COUNT_MATCHING_PRODUCT_IDS,
//        query = "select count(*) from Product p where p.id in (:productIds)"
//)
public class Product {

//    public static final String COUNT_MATCHING_PRODUCT_IDS = "Product.countMatchingProductIds";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_name")
    private String name;

    private double price;

    public Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Double.compare(product.price, price) == 0 && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
