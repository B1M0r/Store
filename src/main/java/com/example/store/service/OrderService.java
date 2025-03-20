package com.example.store.service;

import com.example.store.cache.InMemoryCache;
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
  private final InMemoryCache cache; // Внедряем кэш

  /**
   * Получить все заказы.
   *
   * @return список всех заказов
   */
  public List<Order> getAllOrders() {
    String cacheKey = "all_orders";

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Order>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Order> orders = orderRepository.findAll();
    cache.put(cacheKey, orders); // Сохраняем в кэш
    return orders;
  }

  /**
   * Получить заказ по ID.
   *
   * @param id идентификатор заказа
   * @return заказ с указанным ID
   * @throws RuntimeException если заказ не найден
   */
  public Order getOrderById(Long id) {
    String cacheKey = "order_" + id;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (Order) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order not found"));
    cache.put(cacheKey, order); // Сохраняем в кэш
    return order;
  }

  /**
   * Получить заказы по ID аккаунта.
   *
   * @param accountId идентификатор аккаунта
   * @return список заказов для указанного аккаунта
   */
  public List<Order> getOrdersByAccountId(Long accountId) {
    String cacheKey = "orders_account_" + accountId;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Order>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Order> orders = orderRepository.findByAccountId(accountId);
    cache.put(cacheKey, orders); // Сохраняем в кэш
    return orders;
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

    Order savedOrder = orderRepository.save(order);
    cache.remove("all_orders"); // Очищаем кэш для всех заказов
    cache.remove("orders_account_" + account.getId()); // Очищаем кэш для заказов аккаунта
    return savedOrder;
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
    final Order updatedOrder;
    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products not found");
    }
    order.setId(id);
    order.setProducts(products);

    updatedOrder = orderRepository.save(order);
    cache.remove("all_orders"); // Очищаем кэш для всех заказов
    cache.remove("order_" + id); // Очищаем кэш для конкретного заказа
    cache.remove("orders_account_"
            + order.getAccount().getId()); // Очищаем кэш для заказов аккаунта
    return updatedOrder;
  }

  /**
   * Удалить заказ по ID.
   *
   * @param id идентификатор заказа
   */
  public void deleteOrder(Long id) {
    Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus
                    .NOT_FOUND, "Order not found"));
    orderRepository.delete(order);
    cache.remove("all_orders"); // Очищаем кэш для всех заказов
    cache.remove("order_" + id); // Очищаем кэш для конкретного заказа
    cache.remove("orders_account_"
            + order.getAccount().getId()); // Очищаем кэш для заказов аккаунта
  }
}