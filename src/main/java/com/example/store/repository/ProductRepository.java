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
   * Найти продукты по категории.
   *
   * @param category категория продукта
   * @return список продуктов с указанной категорией
   */
  List<Product> findByCategory(String category);

  /**
   * Найти продукты по цене.
   *
   * @param price цена продукта
   * @return список продуктов с указанной ценой
   */
  List<Product> findByPrice(Integer price);

  /**
   * Найти продукты по категории и цене.
   *
   * @param category категория продукта
   * @param price цена продукта
   * @return список продуктов с указанной категорией и ценой
   */
  @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price = :price")
  List<Product> findByCategoryAndPrice(@Param("category") String category,
                                       @Param("price") Integer price);
}
