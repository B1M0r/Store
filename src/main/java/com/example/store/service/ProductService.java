package com.example.store.service;

import com.example.store.cache.InMemoryCache;
import com.example.store.model.Product;
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

  private static final String CACHE_KEY_ALL_PRODUCTS = "all_products";
  private static final String CACHE_KEY_PRODUCT_PREFIX = "product_";
  private static final String CACHE_KEY_PRODUCTS_CATEGORY_RATING_PREFIX = "products_category_";
  private static final String CACHE_KEY_PRODUCTS_NAME_PRICE_PREFIX = "products_name_";

  private final ProductRepository productRepository;
  private final InMemoryCache cache; // Внедряем кэш

  /**
   * Получить все продукты.
   *
   * @return список всех продуктов
   */
  public List<Product> getAllProducts() {
    // Проверяем кэш
    if (cache.containsKey(CACHE_KEY_ALL_PRODUCTS)) {
      return (List<Product>) cache.get(CACHE_KEY_ALL_PRODUCTS);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Product> products = productRepository.findAll();
    cache.put(CACHE_KEY_ALL_PRODUCTS, products); // Сохраняем в кэш
    return products;
  }

  /**
   * Найти продукты по категории и рейтингу (JPQL).
   *
   * @param category категория продукта
   * @param rating рейтинг продукта
   * @return список продуктов с указанной категорией и рейтингом
   */
  public List<Product> findByCategoryAndRating(String category, double rating) {
    String cacheKey = CACHE_KEY_PRODUCTS_CATEGORY_RATING_PREFIX + category + "_rating_" + rating;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Product>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Product> products = productRepository.findByCategoryAndRating(category, rating);
    cache.put(cacheKey, products); // Сохраняем в кэш
    return products;
  }

  /**
   * Найти продукты по имени и цене (Native Query).
   *
   * @param name название продукта
   * @param price цена продукта
   * @return список продуктов с указанным именем и ценой
   */
  public List<Product> findByNameAndPriceNative(String name, int price) {
    String cacheKey = CACHE_KEY_PRODUCTS_NAME_PRICE_PREFIX + name + "_price_" + price;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Product>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Product> products = productRepository.findByNameAndPriceNative(name, price);
    cache.put(cacheKey, products); // Сохраняем в кэш
    return products;
  }

  /**
   * Получить продукт по ID.
   *
   * @param id идентификатор продукта
   * @return Optional, содержащий продукт, если он найден
   */
  public Optional<Product> getProductById(Long id) {
    String cacheKey = CACHE_KEY_PRODUCT_PREFIX + id;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return Optional.of((Product) cache.get(cacheKey));
    }

    // Если данных нет в кэше, запрашиваем из базы
    Optional<Product> product = productRepository.findById(id);
    product.ifPresent(prod -> cache.put(cacheKey, prod)); // Сохраняем в кэш
    return product;
  }

  /**
   * Сохранить продукт (создание или обновление).
   *
   * @param product данные продукта
   * @return сохраненный продукт
   */
  public Product saveProduct(Product product) {
    Product savedProduct = productRepository.save(product);
    cache.remove(CACHE_KEY_ALL_PRODUCTS); // Очищаем кэш для всех продуктов
    cache.remove(CACHE_KEY_PRODUCT_PREFIX + savedProduct.getId()); // Очищаем кэш для конкретного продукта
    return savedProduct;
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
    productRepository.delete(product);
    cache.remove(CACHE_KEY_ALL_PRODUCTS); // Очищаем кэш для всех продуктов
    cache.remove(CACHE_KEY_PRODUCT_PREFIX + id); // Очищаем кэш для конкретного продукта
  }
}