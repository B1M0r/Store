package com.example.store.repository;

import com.example.store.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Репозиторий для управления сущностями {@link Product}.
 * Предоставляет методы для выполнения операций с продуктами в базе данных.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

  /**
   * Найти продукты по категории и рейтингу (JPQL).
   *
   * @param category категория продукта
   * @param rating рейтинг продукта
   * @return список продуктов с указанной категорией и рейтингом
   */
  @Query("SELECT p FROM Product p WHERE p.category = :category AND p.rating = :rating")
  List<Product> findByCategoryAndRating(
          @Param("category") String category,
          @Param("rating") double rating);

  /**
   * Найти продукты по имени и цене (Native Query).
   *
   * @param name название продукта
   * @param price цена продукта
   * @return список продуктов с указанным именем и ценой
   */
  @Query(value = "SELECT * FROM products p WHERE p.name = :name AND p.price = :price",
          nativeQuery = true)
  List<Product> findByNameAndPriceNative(
          @Param("name") String name,
          @Param("price") int price);
}