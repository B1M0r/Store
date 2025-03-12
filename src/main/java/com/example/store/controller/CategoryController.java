package com.example.store.controller;

import com.example.store.model.Category;
import com.example.store.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления сущностями "Категория".
 * Предоставляет REST API для выполнения CRUD-операций с категориями.
 */
@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * Получить все категории.
   *
   * @return список всех категорий
   */
  @GetMapping
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  /**
   * Получить категорию по ID.
   *
   * @param id идентификатор категории
   * @return категория с указанным ID
   */
  @GetMapping("/{id}")
  public Category getCategoryById(@PathVariable Long id) {
    return categoryService.getCategoryById(id);
  }

  /**
   * Создать новую категорию.
   *
   * @param category данные категории
   * @return созданная категория
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Category createCategory(@RequestBody Category category) {
    return categoryService.createCategory(category);
  }

  /**
   * Обновить существующую категорию.
   *
   * @param id идентификатор категории
   * @param category новые данные категории
   * @return обновленная категория
   */
  @PutMapping("/{id}")
  public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
    return categoryService.updateCategory(id, category);
  }

  /**
   * Удалить категорию по ID.
   *
   * @param id идентификатор категории
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
  }
}