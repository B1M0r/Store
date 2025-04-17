package com.example.store.controller;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.model.Account;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  private Order createTestOrder(Long id) {
    return Order.builder()
            .id(id)
            .orderDate(LocalDateTime.now())
            .totalPrice(100.0)
            .account(Account.builder().id(1L).build())
            .products(List.of(Product.builder().id(1L).build()))
            .productIds(List.of(1L))
            .build();
  }

  @Test
  void getAllOrders_shouldReturnOrdersList() {
    Order order = createTestOrder(1L);
    when(orderService.getAllOrders()).thenReturn(List.of(order));

    ResponseEntity<List<Order>> response = orderController.getAllOrders();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getOrderById_shouldReturnOrder() {
    Order order = createTestOrder(1L);
    when(orderService.getOrderById(1L)).thenReturn(order);

    ResponseEntity<Order> response = orderController.getOrderById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void createOrder_shouldReturnCreatedOrder() {
    Order newOrder = createTestOrder(null);
    Order savedOrder = createTestOrder(1L);
    when(orderService.createOrder(newOrder)).thenReturn(savedOrder);

    ResponseEntity<Order> response = orderController.createOrder(newOrder);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void createOrder_shouldThrowException_whenNoProductIds() {
    Order newOrder = createTestOrder(null);
    newOrder.setProductIds(null);

    ValidationException exception = assertThrows(ValidationException.class,
            () -> orderController.createOrder(newOrder));
    assertTrue(exception.getMessage().contains("Необходимо указать ID продуктов"));
  }

  @Test
  void updateOrder_shouldReturnUpdatedOrder() {
    Order updatedOrder = createTestOrder(1L);
    when(orderService.updateOrder(1L, updatedOrder)).thenReturn(updatedOrder);

    ResponseEntity<Order> response = orderController.updateOrder(1L, updatedOrder);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  void deleteOrder_shouldReturnNoContent() {
    doNothing().when(orderService).deleteOrder(1L);

    ResponseEntity<Void> response = orderController.deleteOrder(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(orderService, times(1)).deleteOrder(1L);
  }

  @Test
  void getOrdersByProductCategoryJpql_shouldReturnOrders() {
    Order order = createTestOrder(1L);
    when(orderService.getOrdersByProductCategoryJpql("electronics")).thenReturn(List.of(order));

    ResponseEntity<List<Order>> response = orderController.getOrdersByProductCategoryJpql("electronics");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getOrdersByProductPriceNative_shouldReturnOrders() {
    Order order = createTestOrder(1L);
    when(orderService.getOrdersByProductPriceNative(100)).thenReturn(List.of(order));

    ResponseEntity<List<Order>> response = orderController.getOrdersByProductPriceNative(100);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }
}