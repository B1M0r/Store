package com.example.store.controller;

import com.example.store.model.Product;
import com.example.store.service.ProductService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для управления сущностями "Продукт".
 * Предоставляет REST API для выполнения CRUD-операций с продуктами.
 */
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * Получить все продукты.
   *
   * @return список всех продуктов
   */
  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  /**
   * Получить продукт по ID.
   *
   * @param id идентификатор продукта
   * @return продукт с указанным ID
   * @throws ResponseStatusException если продукт не найден
   */
  @GetMapping("/{id}")
  public Product getProductById(@PathVariable Long id) {
    return productService.getProductById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus
                    .NOT_FOUND, "Product not found"));
  }

  /**
   * Создать новый продукт.
   *
   * @param product данные продукта
   * @return созданный продукт
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@RequestBody Product product) {
    return productService.saveProduct(product);
  }

  /**
   * Обновить существующий продукт.
   *
   * @param id идентификатор продукта
   * @param product новые данные продукта
   * @return обновленный продукт
   */
  @PutMapping("/{id}")
  public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
    product.setId(id);
    return productService.saveProduct(product);
  }

  /**
   * Удалить продукт по ID.
   *
   * @param id идентификатор продукта
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
  }
}