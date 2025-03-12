package com.example.store.service;

import com.example.store.model.Category;
import com.example.store.repository.CategoryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления сущностями {@link Category}.
 * Предоставляет методы для выполнения операций с категориями.
 */
@Service
@AllArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  /**
   * Получить все категории.
   *
   * @return список всех категорий
   */
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  /**
   * Получить категорию по ID.
   *
   * @param id идентификатор категории
   * @return категория с указанным ID
   * @throws RuntimeException если категория не найдена
   */
  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
  }

  /**
   * Создать новую категорию.
   *
   * @param category данные категории
   * @return созданная категория
   */
  public Category createCategory(Category category) {
    return categoryRepository.save(category);
  }

  /**
   * Обновить существующую категорию.
   *
   * @param id идентификатор категории
   * @param category новые данные категории
   * @return обновленная категория
   */
  public Category updateCategory(Long id, Category category) {
    category.setId(id);
    return categoryRepository.save(category);
  }

  /**
   * Удалить категорию по ID.
   *
   * @param id идентификатор категории
   */
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }
}