package com.example.store.service;

import com.example.store.cache.ProductCache;
import com.example.store.model.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для работы с продуктами.
 *
 * <p>Предоставляет методы для поиска, сохранения и удаления продуктов с поддержкой кэширования.
 */
@Service
@AllArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final ProductCache productCache;

  /**
   * Получает список продуктов с возможностью фильтрации по категории и/или цене.
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
    }
    return productRepository.findAll();
  }

  /**
   * Находит продукт по идентификатору с использованием кэша.
   *
   * @param id идентификатор продукта
   * @return Optional с продуктом, если найден
   */
  public Optional<Product> getProductById(Long id) {
    // Сначала ищем в кэше
    Product cachedProduct = productCache.get(id);
    if (cachedProduct != null) {
      return Optional.of(cachedProduct);
    }

    // Если в кэше нет, получаем из БД
    Optional<Product> product = productRepository.findById(id);

    // Если нашли, кладем в кэш
    product.ifPresent(productCache::put);

    return product;
  }

  /**
   * Сохраняет продукт в БД и обновляет кэш.
   *
   * @param product продукт для сохранения
   * @return сохраненный продукт
   */
  public Product saveProduct(Product product) {
    Product savedProduct = productRepository.save(product);
    productCache.put(savedProduct); // Обновляем кэш
    return savedProduct;
  }

  /**
   * Удаляет продукт по идентификатору.
   *
   * <p>Удаляет продукт из всех заказов, БД и кэша.
   *
   * @param id идентификатор продукта для удаления
   * @throws RuntimeException если продукт не найден
   */
  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // Удаляем продукт из всех заказов
    orderRepository.findByProductsContaining(product).forEach(order -> {
      order.getProducts().remove(product);
      orderRepository.save(order);
    });

    productRepository.delete(product);
    productCache.remove(id); // Удаляем из кэша
  }
}