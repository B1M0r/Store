package com.example.store.service;

import com.example.store.model.Product;
import com.example.store.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления сущностями {@link Product}.
 * Предоставляет методы для выполнения операций с продуктами.
 */
@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  /**
   * Поиск продуктов по категории и/или цене.
   *
   * @param category категория продукта (опционально)
   * @param price цена продукта (опционально)
   * @return список продуктов, соответствующих критериям
   */
  public List<Product> getProducts(String category, Integer price) {
    if (category != null && price != null) {
      return productRepository.findByCategoryAndPrice(category, price);
    } else if (category != null) {
      return productRepository.findByCategory(category);
    } else if (price != null) {
      return productRepository.findByPrice(price);
    } else {
      return productRepository.findAll();
    }
  }

  /**
   * Получить продукт по ID.
   *
   * @param id идентификатор продукта
   * @return Optional, содержащий продукт, если он найден
   */
  public Optional<Product> getProductById(Long id) {
    return productRepository.findById(id);
  }

  /**
   * Сохранить продукт (создание или обновление).
   *
   * @param product данные продукта
   * @return сохраненный продукт
   */
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  /**
   * Удалить продукт по ID.
   *
   * @param id идентификатор продукта
   */
  public void deleteProduct(Long id) {
    productRepository.deleteById(id);
  }
}