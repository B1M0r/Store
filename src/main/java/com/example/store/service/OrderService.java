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
   * @throws ResponseStatusException если заказ не найден
   */
  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
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
   * @throws ResponseStatusException если не указан ID аккаунта или не найдены продукты
   */
  public Order createOrder(Order order) {
    // Проверка наличия ID аккаунта
    if (order.getAccount() == null || order.getAccount().getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account ID is required");
    }

    // Поиск аккаунта в базе данных
    Account account = accountRepository.findById(order.getAccount().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found with id: " + order.getAccount().getId()));
    order.setAccount(account);

    // Проверка наличия productIds
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product IDs are required");
    }

    // Поиск продуктов по их ID
    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products not found");
    }

    // Установка списка продуктов в заказ
    order.setProducts(products);

    // Сохранение заказа в базе данных
    return orderRepository.save(order);
  }

  /**
   * Обновить существующий заказ.
   *
   * @param id идентификатор заказа
   * @param order новые данные заказа
   * @return обновленный заказ
   * @throws ResponseStatusException если не найдены продукты
   */
  public Order updateOrder(Long id, Order order) {
    // Проверка наличия productIds
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product IDs are required");
    }

    // Поиск продуктов по их ID
    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "One or more products not found");
    }

    // Поиск существующего заказа
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

    // Обновление данных заказа
    existingOrder.setOrderDate(order.getOrderDate());
    existingOrder.setTotalPrice(order.getTotalPrice());
    existingOrder.setProducts(products);

    // Сохранение обновленного заказа
    return orderRepository.save(existingOrder);
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