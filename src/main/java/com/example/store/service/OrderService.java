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
 *
 * <p>Предоставляет методы для выполнения операций с заказами.
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
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Order not found"));
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
    if (order.getAccount() == null || order.getAccount().getId() == null) {
      throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Account ID is required");
    }

    Account account = accountRepository.findById(order.getAccount().getId())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account not found with id: "
                    + order.getAccount().getId()));
    order.setAccount(account);

    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "One or more products not found");
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
   * @throws ResponseStatusException если не найдены продукты
   */
  public Order updateOrder(Long id, Order order) {
    List<Product> products = productRepository.findAllById(order.getProductIds());
    if (products.size() != order.getProductIds().size()) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "One or more products not found");
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

  /**
   * Найти заказы по категории продукта (JPQL).
   *
   * @param category категория продукта
   * @return список заказов, содержащих продукты указанной категории
   * @throws ResponseStatusException если заказы не найдены
   */
  public List<Order> getOrdersByProductCategoryJpql(String category) {
    List<Order> orders = orderRepository.findOrdersByProductCategoryJpql(category);
    if (orders.isEmpty()) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Orders with category '" + category + "' not found");
    }
    return orders;
  }

  /**
   * Найти заказы по цене продукта (Native Query).
   *
   * @param price цена продукта
   * @return список заказов, содержащих продукты с указанной ценой
   * @throws ResponseStatusException если заказы не найдены
   */
  public List<Order> getOrdersByProductPriceNative(Integer price) {
    List<Order> orders = orderRepository.findOrdersByProductPriceNative(price);
    if (orders.isEmpty()) {
      throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Orders with product price " + price + " not found");
    }
    return orders;
  }
}