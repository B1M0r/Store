package com.example.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения StoreApplication.
 * Запускает Spring Boot приложение и инициализирует контекст.
 */
@SpringBootApplication
public class StoreApplication {

  /**
   * Точка входа в приложение.
   *
   * @param args аргументы командной строки
   */
  public static void main(String[] args) {
    SpringApplication.run(StoreApplication.class, args);
  }

}