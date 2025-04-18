package com.example.store.service;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.model.Account;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.AccountRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private OrderService orderService;

  private Order testOrder;
  private Account testAccount;
  private Product testProduct;

  @BeforeEach
  void setup() {
    testAccount = new Account();
    testAccount.setId(1L);

    testProduct = new Product();
    testProduct.setId(1L);

    testOrder = Order.builder()
            .id(1L)
            .orderDate(LocalDateTime.now())
            .totalPrice(100.0)
            .account(testAccount)
            .productIds(List.of(1L))
            .products(List.of(testProduct))
            .build();
  }

  @Test
  void testGetAllOrders() {
    when(orderRepository.findAll()).thenReturn(List.of(testOrder));

    List<Order> orders = orderService.getAllOrders();

    assertThat(orders).hasSize(1);
    assertThat(orders.get(0).getId()).isEqualTo(testOrder.getId());
  }

  @Test
  void testGetOrderById_Found() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

    Order result = orderService.getOrderById(1L);

    assertThat(result).isEqualTo(testOrder);
  }

  @Test
  void testGetOrderById_NotFound() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> orderService.getOrderById(1L))
            .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testCreateOrder_Success() {
    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
    when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(testProduct));
    when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

    Order createdOrder = orderService.createOrder(testOrder);

    assertThat(createdOrder).isEqualTo(testOrder);
    verify(orderRepository, times(1)).save(any(Order.class));
  }

  @Test
  void testUpdateOrder_Success() {
    Order updated = testOrder;
    updated.setTotalPrice(200.0);

    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
    when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
    when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(testProduct));
    when(orderRepository.save(any(Order.class))).thenReturn(updated);

    Order result = orderService.updateOrder(1L, updated);

    assertThat(result.getTotalPrice()).isEqualTo(200.0);
  }

  @Test
  void testUpdateOrder_NotFound() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> orderService.updateOrder(1L, testOrder))
            .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testDeleteOrder() {
    when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

    orderService.deleteOrder(1L);

    verify(orderRepository, times(1)).delete(testOrder);
  }

  @Test
  void testDeleteOrder_NotFound() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> orderService.deleteOrder(1L))
            .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void testGetOrdersByCategoryJpql() {
    when(orderRepository.findOrdersByProductCategoryJpql("books")).thenReturn(List.of(testOrder));

    List<Order> result = orderService.getOrdersByProductCategoryJpql("books");

    assertThat(result).containsExactly(testOrder);
  }

  @Test
  void testGetOrdersByPriceNative() {
    when(orderRepository.findOrdersByProductPriceNative(50)).thenReturn(List.of(testOrder));

    List<Order> result = orderService.getOrdersByProductPriceNative(50);

    assertThat(result).containsExactly(testOrder);
  }
}
