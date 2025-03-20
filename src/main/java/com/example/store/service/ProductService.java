package com.example.store.service;

import com.example.store.model.Order;
import com.example.store.model.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для управления сущностями {@link Product}.
 * Предоставляет методы для выполнения операций с продуктами.
 */
@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

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
   * @throws RuntimeException если продукт не найден
   */
  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // Удаляем продукт из всех заказов
    List<Order> orders = orderRepository.findByProductsContaining(product);
    for (Order order : orders) {
      order.getProducts().remove(product); // Удаляем продукт из заказа
      orderRepository.save(order); // Сохраняем обновлённый заказ
    }

    productRepository.delete(product); // Удаляем продукт
  }
}