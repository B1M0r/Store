package com.example.store.repository;

import com.example.store.model.Order;
import com.example.store.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

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
   * Найти заказы, содержащие определённый продукт.
   *
   * @param product продукт
   * @return список заказов, содержащих продукт
   */
  List<Order> findByProductsContaining(Product product);
}