package com.example.store.service;

import com.example.store.model.Account;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.AccountRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

  private Order createTestOrder(Long id, Account account, List<Product> products) {
    return Order.builder()
            .id(id)
            .orderDate(LocalDateTime.now())
            .totalPrice(100.0)
            .account(account)
            .products(products)
            .productIds(products.stream().map(Product::getId).toList())
            .build();
  }

  private Account createTestAccount(Long id) {
    return Account.builder().id(id).build();
  }

  private Product createTestProduct(Long id) {
    return Product.builder().id(id).build();
  }

  @Test
  void getAllOrders_shouldReturnAllOrders() {
    Order order1 = createTestOrder(1L, createTestAccount(1L), List.of(createTestProduct(1L)));
    Order order2 = createTestOrder(2L, createTestAccount(2L), List.of(createTestProduct(2L)));
    when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

    List<Order> result = orderService.getAllOrders();

    assertEquals(2, result.size());
    verify(orderRepository, times(1)).findAll();
  }

  @Test
  void getOrderById_shouldReturnOrder_whenExists() {
    Long orderId = 1L;
    Order mockOrder = createTestOrder(orderId, createTestAccount(1L), List.of(createTestProduct(1L)));
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

    Order result = orderService.getOrderById(orderId);

    assertEquals(orderId, result.getId());
    verify(orderRepository, times(1)).findById(orderId);
  }

  @Test
  void getOrderById_shouldThrowException_whenNotExists() {
    Long orderId = 99L;
    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.getOrderById(orderId));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void getOrdersByAccountId_shouldReturnOrdersForAccount() {
    Long accountId = 1L;
    Order order1 = createTestOrder(1L, createTestAccount(accountId), List.of(createTestProduct(1L)));
    Order order2 = createTestOrder(2L, createTestAccount(accountId), List.of(createTestProduct(2L)));
    when(orderRepository.findByAccountId(accountId)).thenReturn(List.of(order1, order2));

    List<Order> result = orderService.getOrdersByAccountId(accountId);

    assertEquals(2, result.size());
    verify(orderRepository, times(1)).findByAccountId(accountId);
  }

  @Test
  void getOrdersByProductCategoryJpql_shouldThrowException_whenNoOrdersFound() {
    String category = "nonexistent";
    when(orderRepository.findOrdersByProductCategoryJpql(category)).thenReturn(Collections.emptyList());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.getOrdersByProductCategoryJpql(category));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void getOrdersByProductPriceNative_shouldThrowException_whenNoOrdersFound() {
    Integer price = 9999;
    when(orderRepository.findOrdersByProductPriceNative(price)).thenReturn(Collections.emptyList());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.getOrdersByProductPriceNative(price));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void createOrder_shouldSuccess_whenValidData() {
    Account account = createTestAccount(1L);
    Product product = createTestProduct(1L);
    Order newOrder = createTestOrder(null, account, List.of(product));

    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
    when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(product));
    when(orderRepository.save(newOrder)).thenReturn(newOrder);

    Order result = orderService.createOrder(newOrder);

    assertNotNull(result);
    verify(orderRepository, times(1)).save(newOrder);
  }

  @Test
  void createOrder_shouldThrowException_whenAccountNotFound() {
    Order newOrder = createTestOrder(null, createTestAccount(99L), List.of(createTestProduct(1L)));
    when(accountRepository.findById(99L)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.createOrder(newOrder));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void createOrder_shouldThrowException_whenProductsNotFound() {
    Account account = createTestAccount(1L);
    Order newOrder = createTestOrder(null, account, List.of(createTestProduct(1L)));
    newOrder.setProductIds(List.of(1L, 2L)); // Указываем 2 продукта, но найдем только 1

    when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
    when(productRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(createTestProduct(1L)));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.createOrder(newOrder));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void createOrder_shouldThrowException_whenAccountIdNotProvided() {
    Order newOrder = new Order();
    newOrder.setAccount(null);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> orderService.createOrder(newOrder));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
  }

  @Test
  void updateOrder_shouldSuccess_whenValidData() {
    Long orderId = 1L;
    Product product = createTestProduct(1L);
    Order existingOrder = createTestOrder(orderId, createTestAccount(1L), List.of(product));

    when(productRepository.findAllById(List.of(1L))).thenReturn(List.of(product));
    when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

    Order result = orderService.updateOrder(orderId, existingOrder);

    assertEquals(orderId, result.getId());
    verify(orderRepository, times(1)).save(existingOrder);
  }

  @Test
  void deleteOrder_shouldCallRepository() {
    Long orderId = 1L;
    doNothing().when(orderRepository).deleteById(orderId);

    assertDoesNotThrow(() -> orderService.deleteOrder(orderId));
    verify(orderRepository, times(1)).deleteById(orderId);
  }

  @Test
  void getOrdersByProductCategoryJpql_shouldReturnOrders() {
    String category = "electronics";
    Order order = createTestOrder(1L, createTestAccount(1L), List.of(createTestProduct(1L)));
    when(orderRepository.findOrdersByProductCategoryJpql(category)).thenReturn(List.of(order));

    List<Order> result = orderService.getOrdersByProductCategoryJpql(category);

    assertEquals(1, result.size());
    verify(orderRepository, times(1)).findOrdersByProductCategoryJpql(category);
  }

  @Test
  void getOrdersByProductPriceNative_shouldReturnOrders() {
    Integer price = 100;
    Order order = createTestOrder(1L, createTestAccount(1L), List.of(createTestProduct(1L)));
    when(orderRepository.findOrdersByProductPriceNative(price)).thenReturn(List.of(order));

    List<Order> result = orderService.getOrdersByProductPriceNative(price);

    assertEquals(1, result.size());
    verify(orderRepository, times(1)).findOrdersByProductPriceNative(price);
  }
}