package com.example.store.repository;

import com.example.store.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Репозиторий для управления сущностями {@link Order}.
 * Предоставляет методы для выполнения операций с заказами в базе данных.
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
   * Найти заказы, содержащие определённый продукт, с использованием JPQL запроса.
   *
   * @param productId идентификатор продукта
   * @return список заказов, содержащих указанный продукт
   */
  @Query("SELECT o FROM Order o JOIN o.products p WHERE p.id = :productId")
  List<Order> findOrdersByProductIdJpql(@Param("productId") Long productId);

  /**
   * Найти заказы, содержащие определённый продукт, с использованием нативного SQL запроса.
   *
   * @param productId идентификатор продукта
   * @return список заказов, содержащих указанный продукт
   */
  @Query(value = "SELECT * FROM orders o JOIN order_product "
          + "op ON o.id = op.order_id WHERE op.product_id = :productId",
          nativeQuery = true)
  List<Order> findOrdersByProductIdNative(@Param("productId") Long productId);
}