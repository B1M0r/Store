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

  private final ProductRepository productRepository;
  private final InMemoryCache cache; // Внедряем кэш

  /**
   * Получить все продукты.
   *
   * @return список всех продуктов
   */
  public List<Product> getAllProducts() {
    String cacheKey = "all_products";

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Product>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Product> products = productRepository.findAll();
    cache.put(cacheKey, products); // Сохраняем в кэш
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
    String cacheKey = "products_category_" + category + "_rating_" + rating;

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
    String cacheKey = "products_name_" + name + "_price_" + price;

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
    String cacheKey = "product_" + id;

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
    cache.remove("all_products"); // Очищаем кэш для всех продуктов
    cache.remove("product_" + savedProduct.getId()); // Очищаем кэш для конкретного продукта
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
    cache.remove("all_products"); // Очищаем кэш для всех продуктов
    cache.remove("product_" + id); // Очищаем кэш для конкретного продукта
  }
}