package com.example.store.service;

import com.example.store.model.Product;
import java.util.List;
import org.springframework.stereotype.Service;



/**
 * Сервис для работы с продуктами.
 * Предоставляет методы для получения информации о продуктах.
 */
@Service
public class ProductService {

  private final List<Product> products = List.of(
          Product.builder().id(1).name("Laptop").category("Electronics").build(),
          Product.builder().id(2).name("Smartphone").category("Electronics").build(),
          Product.builder().id(4).name("T-Shirt").category("Clothing").build(),
          Product.builder().id(5).name("Jeans").category("Clothing").build()
  );

  /**
   * Возвращает список продуктов, отфильтрованных по категории и/или идентификатору.
   *
   * @param category категория продукта (опционально)
   * @param id идентификатор продукта (опционально)
   * @return список продуктов, соответствующих критериям фильтрации
   */
  public List<Product> getProducts(String category, Integer id) {
    return products.stream()
            .filter(product -> (category == null || product.getCategory()
                    .equalsIgnoreCase(category)) && (id == null || product.getId() == id))
            .toList();
  }
}
