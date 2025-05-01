package com.example.store.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Сервис для подсчета количества посещений различных URL.
 *
 * <p>Предоставляет потокобезопасные методы для:
 * <ul>
 *   <li>Инкрементации счетчика посещений для конкретного URL
 *   <li>Получения количества посещений для конкретного URL
 *   <li>Получения полной статистики посещений всех URL
 * </ul>
 */
@Service
public class VisitCounterService {
  private final ConcurrentHashMap<String, AtomicLong> urlCounters = new ConcurrentHashMap<>();

  /**
   * Увеличивает счетчик посещений для указанного URL.
   *
   * @param url URL для увеличения счетчика
   * @throws IllegalArgumentException если url равен null
   */
  public void incrementCounter(String url) {
    if (url == null) {
      throw new IllegalArgumentException("URL не может быть null");
    }
    urlCounters.computeIfAbsent(url, k -> new AtomicLong(0)).incrementAndGet();
  }

  /**
   * Возвращает количество посещений для указанного URL.
   *
   * @param url URL для проверки
   * @return количество посещений (0 если URL не встречался)
   * @throws IllegalArgumentException если url равен null
   */
  public long getCount(String url) {
    if (url == null) {
      throw new IllegalArgumentException("URL не может быть null");
    }
    return urlCounters.getOrDefault(url, new AtomicLong(0)).get();
  }

  /**
   * Возвращает полную статистику посещений всех URL.
   *
   * @return неизменяемая Map, где ключ - URL, значение - количество посещений
   */
  public Map<String, Long> getAllCounters() {
    return urlCounters.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().get()
            ));
  }
}