package com.example.hardwarestore.orders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        orderController = new OrderController(orderService);
    }

    @Test
    public void createOrderReturnsABadRequestIfAnExceptionIsThrown() {
        Order order = new Order();
        order.setProducts(emptyList());

        when(orderService.createOrder(eq(order))).thenThrow(new OrderServiceException("Bad order"));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> orderController.createOrder(order));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

}
