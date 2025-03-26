package com.example.store.repository;

import com.example.store.model.Order;
import com.example.store.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Репозиторий для управления сущностями {@link Order}.
 *
 * <p>Предоставляет методы для выполнения операций с заказами в базе данных.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  /**
   * Найти заказы по ID аккаунта.
   *
   * @param accountId идентификатор аккаунта
   * @return список заказов для указанного аккаунта
   */
  List<Order> findByAccountId(Long accountId);

  /**
   * Найти заказы, содержащие определённый продукт.
   *
   * @param product продукт
   * @return список заказов, содержащих продукт
   */
  List<Order> findByProductsContaining(Product product);

  /**
   * Найти заказы по категории продукта (JPQL запрос).
   *
   * @param category категория продукта
   * @return список заказов, содержащих продукты указанной категории
   */
  @Query("SELECT o FROM Order o JOIN o.products p WHERE p.category = :category")
  List<Order> findOrdersByProductCategoryJpql(String category);

  /**
   * Найти заказы по цене продукта (Native SQL запрос).
   *
   * @param price цена продукта
   * @return список заказов, содержащих продукты с указанной ценой
   */
  @Query(
          value =
                  """
                  SELECT DISTINCT o.* FROM orders o
                  JOIN order_product op ON o.id = op.order_id
                  JOIN products p ON op.product_id = p.id
                  WHERE p.price = :price
                  """,
          nativeQuery = true)
  List<Order> findOrdersByProductPriceNative(Integer price);
}