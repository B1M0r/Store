package com.example.store.service;

import com.example.store.model.Account;
import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.AccountRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Сервис для управления сущностями {@link Order}.
 * Предоставляет методы для выполнения операций с заказами.
 */
@Service
@AllArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final AccountRepository accountRepository;

  /**
   * Получить все заказы.
   *
   * @return список всех заказов
   */
  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  /**
   * Получить заказ по ID.
   *
   * @param id идентификатор заказа
   * @return заказ с указанным ID
   * @throws RuntimeException если заказ не найден
   */
  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
  }

  /**
   * Получить заказы по ID аккаунта.
   *
   * @param accountId идентификатор аккаунта
   * @return список заказов для указанного аккаунта
   */
  public List<Order> getOrdersByAccountId(Long accountId) {
    return orderRepository.findByAccountId(accountId);
  }

  /**
   * Создать новый заказ.
   *
   * @param order данные заказа
   * @return созданный заказ
   * @throws RuntimeException если не указан ID аккаунта или не найдены продукты
   */
  public Order createOrder(Order order) {
    if (order.getAccount() == null || order.getAccount().getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID is required");
    }

    Account account = accountRepository.findById(order.getAccount().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus
                    .NOT_FOUND, "Account not found with id: " + order.getAccount().getId()));
    order.setAccount(account);

    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products not found");
    }
    order.setProducts(products);

    return orderRepository.save(order);
  }

  /**
   * Обновить существующий заказ.
   *
   * @param id идентификатор заказа
   * @param order новые данные заказа
   * @return обновленный заказ
   * @throws RuntimeException если не найдены продукты
   */
  public Order updateOrder(Long id, Order order) {
    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products not found");
    }
    order.setId(id);
    order.setProducts(products);
    return orderRepository.save(order);
  }

  /**
   * Удалить заказ по ID.
   *
   * @param id идентификатор заказа
   */
  public void deleteOrder(Long id) {
    orderRepository.deleteById(id);
  }
}