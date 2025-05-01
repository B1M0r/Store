package com.example.store.aspect;

import com.example.store.service.VisitCounterService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект для отслеживания посещений API endpoints.
 * Считает количество вызовов методов контроллеров.
 */
@Aspect
@Component
public class VisitTrackingAspect {
  private final VisitCounterService visitCounterService;

  /**
   * Создает экземпляр аспекта для отслеживания посещений.
   *
   * @param visitCounterService сервис для подсчета посещений
   */
  public VisitTrackingAspect(VisitCounterService visitCounterService) {
    this.visitCounterService = visitCounterService;
  }

  /**
   * Увеличивает счетчик общих посещений для методов контроллеров.
   * Срабатывает перед выполнением методов с аннотацией @RequestMapping.
   */
  @Before("execution(* com.example.store.controller.*.*(..))"
          + " && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
  public void trackVisit() {
    visitCounterService.incrementCounter("general");
  }

  /**
   * Увеличивает счетчик GET-запросов.
   * Срабатывает перед выполнением методов с аннотацией @GetMapping.
   */
  @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
  public void trackGetVisit() {
    visitCounterService.incrementCounter("GET");
  }

  /**
   * Увеличивает счетчик POST-запросов.
   * Срабатывает перед выполнением методов с аннотацией @PostMapping.
   */
  @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  public void trackPostVisit() {
    visitCounterService.incrementCounter("POST");
  }
}