package com.example.store.repository;

import com.example.store.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для управления сущностями {@link Category}.
 * Предоставляет методы для выполнения операций с категориями в базе данных.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

  /**
   * Найти категории по названию.
   *
   * @param name название категории
   * @return список категорий с указанным названием
   */
  List<Category> findByName(String name);
}