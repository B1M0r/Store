package com.example.store.controller;

import com.example.store.model.Order;
import com.example.store.service.OrderService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для управления сущностями "Заказ".
 *
 * <p>Предоставляет REST API для выполнения CRUD-операций с заказами.
 */
@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

  private final OrderService orderService;

  /**
   * Получить все заказы.
   *
   * @return список всех заказов
   */
  @GetMapping
  public List<Order> getAllOrders() {
    return orderService.getAllOrders();
  }

  /**
   * Получить заказ по ID.
   *
   * @param id идентификатор заказа
   * @return заказ с указанным ID
   */
  @GetMapping("/{id}")
  public Order getOrderById(@PathVariable Long id) {
    return orderService.getOrderById(id);
  }

  /**
   * Создать новый заказ.
   *
   * @param order данные заказа
   * @return созданный заказ
   * @throws ResponseStatusException если не указаны ID продуктов
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Order createOrder(@RequestBody Order order) {
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product IDs are required");
    }
    return orderService.createOrder(order);
  }

  /**
   * Обновить существующий заказ.
   *
   * @param id идентификатор заказа
   * @param order новые данные заказа
   * @return обновленный заказ
   * @throws ResponseStatusException если не указаны ID продуктов
   */
  @PutMapping("/{id}")
  public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product IDs are required");
    }
    return orderService.updateOrder(id, order);
  }

  /**
   * Удалить заказ по ID.
   *
   * @param id идентификатор заказа
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
  }

  /**
   * Поиск заказов по категории продукта (JPQL).
   *
   * @param category категория продукта
   * @return список заказов, содержащих продукты указанной категории
   */
  @GetMapping("/filter/by-category-jpql")
  public List<Order> getOrdersByProductCategoryJpql(@RequestParam("category") String category) {
    return orderService.getOrdersByProductCategoryJpql(category);
  }

  /**
   * Поиск заказов по цене продукта (Native Query).
   *
   * @param price цена продукта
   * @return список заказов, содержащих продукты с указанной ценой
   */
  @GetMapping("/filter/by-price-native")
  public List<Order> getOrdersByProductPriceNative(@RequestParam("price") Integer price) {
    return orderService.getOrdersByProductPriceNative(price);
  }
}