package com.example.store.controller;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.exception.ValidationException;
import com.example.store.model.Order;
import com.example.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления заказами.
 * Предоставляет REST API для операций с сущностью {@link Order}.
 */
@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "Order Controller", description = "API для управления заказами")
public class OrderController {

  private final OrderService orderService;

  /**
   * Получает список всех заказов.
   *
   * @return ResponseEntity со списком всех заказов
   */
  @GetMapping
  @Operation(
          summary = "Получить все заказы",
          description = "Возвращает список всех заказов")
  @ApiResponse(
          responseCode = "200",
          description = "Успешный запрос",
          content = @Content(schema = @Schema(implementation = Order.class)))
  public ResponseEntity<List<Order>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  /**
   * Получает заказ по идентификатору.
   *
   * @param id идентификатор заказа
   * @return ResponseEntity с найденным заказом
   */
  @GetMapping("/{id}")
  @Operation(
          summary = "Получить заказ по ID",
          description = "Возвращает заказ по указанному ID")
  @ApiResponse(
          responseCode = "200",
          description = "Заказ найден",
          content = @Content(schema = @Schema(implementation = Order.class)))
  @ApiResponse(
          responseCode = "404",
          description = "Заказ не найден")
  public ResponseEntity<Order> getOrderById(
          @Parameter(
                  description = "ID заказа",
                  example = "1",
                  required = true)
          @PathVariable Long id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  /**
   * Создает новый заказ.
   *
   * @param order данные нового заказа
   * @return ResponseEntity с созданным заказом
   * @throws ValidationException если не указаны ID продуктов
   */
  @PostMapping
  @Operation(
          summary = "Создать заказ",
          description = "Создает новый заказ")
  @ApiResponse(
          responseCode = "201",
          description = "Заказ создан",
          content = @Content(schema = @Schema(implementation = Order.class)))
  @ApiResponse(
          responseCode = "400",
          description = "Некорректные данные")
  public ResponseEntity<Order> createOrder(
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Данные заказа",
                  required = true,
                  content = @Content(schema = @Schema(implementation = Order.class)))
          @Valid @RequestBody Order order) {
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ValidationException("Необходимо указать ID продуктов");
    }
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderService.createOrder(order));
  }

  /**
   * Обновляет существующий заказ.
   *
   * @param id идентификатор заказа
   * @param order новые данные заказа
   * @return ResponseEntity с обновленным заказом
   * @throws ValidationException если не указаны ID продуктов
   */
  @PutMapping("/{id}")
  @Operation(
          summary = "Обновить заказ",
          description = "Обновляет существующий заказ")
  @ApiResponse(
          responseCode = "200",
          description = "Заказ обновлен",
          content = @Content(schema = @Schema(implementation = Order.class)))
  @ApiResponse(
          responseCode = "400",
          description = "Некорректные данные")
  @ApiResponse(
          responseCode = "404",
          description = "Заказ не найден")
  public ResponseEntity<Order> updateOrder(
          @Parameter(
                  description = "ID заказа",
                  example = "1",
                  required = true)
          @PathVariable Long id,
          @Valid @RequestBody Order order) {
    if (order.getProductIds() == null || order.getProductIds().isEmpty()) {
      throw new ValidationException("Необходимо указать ID продуктов");
    }
    return ResponseEntity.ok(orderService.updateOrder(id, order));
  }

  /**
   * Удаляет заказ по идентификатору.
   *
   * @param id идентификатор удаляемого заказа
   * @return ResponseEntity без содержимого
   */
  @DeleteMapping("/{id}")
  @Operation(
          summary = "Удалить заказ",
          description = "Удаляет заказ по ID")
  @ApiResponse(
          responseCode = "204",
          description = "Заказ удален")
  @ApiResponse(
          responseCode = "404",
          description = "Заказ не найден")
  public ResponseEntity<Void> deleteOrder(
          @Parameter(
                  description = "ID заказа",
                  example = "1",
                  required = true)
          @PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Фильтрует заказы по категории продукта (JPQL).
   *
   * @param category продукта для фильтрации
   * @return ResponseEntity со списком отфильтрованных заказов
   */
  @GetMapping("/filter/by-category-jpql")
  @Operation(
          summary = "Фильтр заказов по категории (JPQL)",
          description = "Возвращает заказы, содержащие продукты указанной категории")
  @ApiResponse(
          responseCode = "200",
          description = "Успешный запрос",
          content = @Content(schema = @Schema(implementation = Order.class)))
  public ResponseEntity<List<Order>> getOrdersByProductCategoryJpql(
          @Parameter(
                  description = "Категория продукта",
                  example = "electronics",
                  required = true)
          @RequestParam String category) {
    return ResponseEntity.ok(orderService.getOrdersByProductCategoryJpql(category));
  }

  /**
   * Фильтрует заказы по цене продукта (Native Query).
   *
   * @param price продукта для фильтрации
   * @return ResponseEntity со списком отфильтрованных заказов
   */
  @GetMapping("/filter/by-price-native")
  @Operation(
          summary = "Фильтр заказов по цене (Native Query)",
          description = "Возвращает заказы, содержащие продукты с указанной ценой")
  @ApiResponse(
          responseCode = "200",
          description = "Успешный запрос",
          content = @Content(schema = @Schema(implementation = Order.class)))
  public ResponseEntity<List<Order>> getOrdersByProductPriceNative(
          @Parameter(
                  description = "Цена продукта",
                  example = "100",
                  required = true)
          @RequestParam Integer price) {
    return ResponseEntity.ok(orderService.getOrdersByProductPriceNative(price));
  }
}