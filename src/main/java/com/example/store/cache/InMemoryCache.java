package com.example.store.cache;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * In-memory кэш для хранения данных.
 */
@Component
public class InMemoryCache {

  private final Map<String, Object> cache = new HashMap<>();

  /**
   * Получить данные из кэша по ключу.
   *
   * @param key ключ для поиска данных
   * @return данные, если они есть в кэше, иначе null
   */
  public Object get(String key) {
    return cache.get(key);
  }

  /**
   * Сохранить данные в кэш.
   *
   * @param key ключ для сохранения данных
   * @param value данные для сохранения
   */
  public void put(String key, Object value) {
    cache.put(key, value);
  }

  /**
   * Проверить наличие данных в кэше по ключу.
   *
   * @param key ключ для проверки
   * @return true, если данные есть в кэше, иначе false
   */
  public boolean containsKey(String key) {
    return cache.containsKey(key);
  }

  /**
   * Удалить данные из кэша по ключу.
   *
   * @param key ключ для удаления данных
   */
  public void remove(String key) {
    cache.remove(key);
  }

  /**
   * Очистить весь кэш.
   */
  public void clear() {
    cache.clear();
  }
}