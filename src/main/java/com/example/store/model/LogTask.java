package com.example.store.model;

import java.time.LocalDateTime;

/**
 * Модель задачи генерации лог-файла.
 *
 * <p>Содержит информацию о:
 * <ul>
 *   <li>Идентификаторе и статусе задачи
 *   <li>Дате, для которой генерируются логи
 *   <li>Пути к сгенерированному файлу
 *   <li>Сообщении об ошибке (если есть)
 *   <li>Времени создания задачи
 * </ul>
 */
public class LogTask {
  private final String id;
  private String status;
  private final String date;
  private String filePath;
  private String errorMessage;
  private final LocalDateTime createdAt;

  /**
   * Создает новую задачу генерации лог-файла.
   *
   * @param id уникальный идентификатор задачи
   * @param status начальный статус задачи
   * @param date дата, для которой генерируются логи (формат yyyy-MM-dd)
   * @param createdAt время создания задачи
   */
  public LogTask(String id, String status, String date, LocalDateTime createdAt) {
    this.id = id;
    this.status = status;
    this.date = date;
    this.createdAt = createdAt;
  }

  /** Возвращает уникальный идентификатор задачи. */
  public String getId() {
    return id;
  }

  /** Возвращает текущий статус задачи. */
  public String getStatus() {
    return status;
  }

  /**
   * Устанавливает новый статус задачи.
   *
   * @param status новый статус задачи
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /** Возвращает дату, для которой генерируются логи. */
  public String getDate() {
    return date;
  }

  /** Возвращает путь к сгенерированному файлу. */
  public String getFilePath() {
    return filePath;
  }

  /**
   * Устанавливает путь к сгенерированному файлу.
   *
   * @param filePath путь к файлу
   */
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  /** Возвращает сообщение об ошибке (если есть). */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Устанавливает сообщение об ошибке.
   *
   * @param errorMessage текст ошибки
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /** Возвращает время создания задачи. */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}