package com.example.hardwarestore.orders;

import com.example.hardwarestore.products.ProductOrder;
import com.example.hardwarestore.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, productRepository);
    }

    @Test
    public void getOrdersFindsAllOrders() {
        orderService.getOrders();

        verify(orderRepository).findAll();
    }

    @Test
    public void createOrderGeneratesACustomerIdIfNoneIsSupplied() {
        Order original = new Order();
        original.setProducts(emptyList());

        when(orderRepository.save(eq(original))).thenReturn(original);

        Order order = orderService.createOrder(original);

        assertNotNull(order.getCustomerId());
    }

    @Test
    public void createOrderUsesExistingCustomerIdIfSupplied() {
        Order original = new Order();
        UUID customerId = UUID.randomUUID();
        original.setProducts(emptyList());
        original.setCustomerId(customerId);

        when(orderRepository.save(eq(original))).thenReturn(original);

        Order order = orderService.createOrder(original);

        assertEquals(customerId, order.getCustomerId());
    }

    @Test
    public void createOrderThrowsExceptionIfAProductHasAMismatchedId() {
        Order original = new Order();
        ProductOrder sledgeHammer = new ProductOrder();
        sledgeHammer.setProductId(1);
        sledgeHammer.setQuantity(2);
        ProductOrder axe = new ProductOrder();
        axe.setProductId(2);
        axe.setQuantity(10);

        original.setProducts(Arrays.asList(sledgeHammer, axe));

        when(productRepository.countMatchingProductIds(anySet())).thenReturn(1);

        assertThrows(OrderServiceException.class, () -> orderService.createOrder(original));
    }

    @Test
    public void createOrderCreatesAnOrder() {
        Order original = new Order();
        ProductOrder sledgeHammer = new ProductOrder();
        sledgeHammer.setProductId(1);
        sledgeHammer.setQuantity(2);
        ProductOrder axe = new ProductOrder();
        axe.setProductId(2);
        axe.setQuantity(10);

        original.setProducts(Arrays.asList(sledgeHammer, axe));

        when(productRepository.countMatchingProductIds(anySet())).thenReturn(2);
        when(orderRepository.save(eq(original))).thenReturn(original);

        Order order = orderService.createOrder(original);

        verify(orderRepository).save(original);

        assertEquals(original, order);
    }

}
