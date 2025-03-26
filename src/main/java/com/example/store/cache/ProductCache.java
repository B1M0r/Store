package com.example.store.cache;

import com.example.store.model.Product;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Кэш для хранения продуктов в памяти.
 *
 * <p>Реализует thread-safe хранение продуктов с базовыми операциями кэширования.
 */
@Component
public class ProductCache {

  private final Map<Long, Product> productCache = new ConcurrentHashMap<>();

  /**
   * Получить продукт из кэша по идентификатору.
   *
   * @param id идентификатор продукта
   * @return продукт или null, если не найден
   */
  public Product get(Long id) {
    return productCache.get(id);
  }

  /**
   * Добавить или обновить продукт в кэше.
   *
   * @param product продукт для кэширования
   */
  public void put(Product product) {
    if (product != null && product.getId() != null) {
      productCache.put(product.getId(), product);
    }
  }

  /**
   * Удалить продукт из кэша по идентификатору.
   *
   * @param id идентификатор продукта для удаления
   */
  public void remove(Long id) {
    productCache.remove(id);
  }

  /**
   * Получить текущий размер кэша.
   *
   * @return количество элементов в кэше
   */
  public int size() {
    return productCache.size();
  }
}