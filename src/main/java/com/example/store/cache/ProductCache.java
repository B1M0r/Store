package com.example.store.cache;

import com.example.store.model.Product;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Кэш для хранения продуктов в памяти.
 *
 * <p>Реализует thread-safe хранение продуктов с базовыми операциями кэширования.
 */
@Slf4j
@Component
public class ProductCache {

  private final Map<Long, Product> cacheProduct = new ConcurrentHashMap<>();

  /**
   * Получить продукт из кэша по идентификатору.
   *
   * @param id идентификатор продукта
   * @return продукт или null, если не найден
   */
  public Product get(Long id) {
    log.info("Get product by id: {}", id);
    return cacheProduct.get(id);
  }

  /**
   * Добавить или обновить продукт в кэше.
   *
   * @param product продукт для кэширования
   */
  public void put(Product product) {
    if (product != null && product.getId() != null) {
      log.info("Put product: {}", product);
      cacheProduct.put(product.getId(), product);
    }
  }

  /**
   * Удалить продукт из кэша по идентификатору.
   *
   * @param id идентификатор продукта для удаления
   */
  public void remove(Long id) {
    log.info("Remove product: {}", id);
    cacheProduct.remove(id);
  }

  /**
   * Получить текущий размер кэша.
   *
   * @return количество элементов в кэше
   */
  public int size() {
    return cacheProduct.size();
  }
}