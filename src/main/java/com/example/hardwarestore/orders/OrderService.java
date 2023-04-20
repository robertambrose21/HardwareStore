package com.example.hardwarestore.orders;

import com.example.hardwarestore.products.ProductOrder;
import com.example.hardwarestore.products.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    private ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        if (order.getCustomerId() == null) {
            order.setCustomerId(UUID.randomUUID());
        }

        Set<Integer> productIds = order.getProducts().stream().map(ProductOrder::getProductId).collect(toSet());
        int numMatchingIds = productRepository.countMatchingProductIds(productIds);

        if(numMatchingIds < productIds.size()) {
            throw new OrderServiceException("Invalid product ids supplied");
        }

        return orderRepository.save(order);
    }

}
