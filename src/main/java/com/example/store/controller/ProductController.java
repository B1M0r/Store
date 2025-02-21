package com.example.store.controller;

import com.example.store.model.Product;
import com.example.store.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для управления продуктами.
 * Предоставляет REST-эндпоинты для получения информации о продуктах с возможностью фильтрации.
 */
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

  private final ProductService productService;

  /**
   * Возвращает список продуктов, отфильтрованных по категории и/или идентификатору.
   *
   * @param category категория продукта (опционально)
   * @param id идентификатор продукта (опционально)
   * @return ResponseEntity с отфильтрованным списком продуктов.
   *         Если продукты не найдены, возвращает статус 404.
   */
  @GetMapping
  public List<Product> getProducts(
          @RequestParam(value = "category", required = false) String category,
          @RequestParam(value = "id", required = false) Integer id) {
    List<Product> filteredProducts = productService.getProducts(category, id);

    if (filteredProducts.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
              "No products found with the specified criteria");
    }

    return filteredProducts;
  }
}
