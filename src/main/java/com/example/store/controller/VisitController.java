package com.example.store.controller;

import com.example.store.service.VisitCounterService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы со статистикой посещений.
 *
 * <p>Предоставляет API для получения информации о количестве запросов к различным endpoint'ам.
 */
@RestController
@RequestMapping("/api/number-of-requests")
public class VisitController {

  private final VisitCounterService visitCounterService;

  /**
   * Создает экземпляр контроллера.
   *
   * @param visitCounterService сервис для работы со счетчиками посещений
   * @throws IllegalArgumentException если visitCounterService равен null
   */
  public VisitController(VisitCounterService visitCounterService) {
    this.visitCounterService = visitCounterService;
  }

  /**
   * Возвращает статистику посещений по всем endpoint'ам.
   *
   * @return Map, где ключ - имя endpoint'а, значение - количество запросов
   */
  @GetMapping
  public Map<String, Long> getVisitCounts() {
    return visitCounterService.getAllCounters();
  }
}