package com.example.store.service;

import com.example.store.model.LogTask;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с лог-файлами.
 *
 * <p>Предоставляет функциональность для:
 * <ul>
 *   <li>Асинхронной генерации лог-файлов
 *   <li>Отслеживания статуса задач
 *   <li>Скачивания сгенерированных файлов
 * </ul>
 */
@Service
public class LogService {
  private final Map<String, LogTask> tasks = new ConcurrentHashMap<>();
  private static final String LOG_DIR = "./generated-logs/";
  private static final String SOURCE_LOG_PATH = "./store.log";

  /**
   * Асинхронно генерирует лог-файл для указанной даты.
   *
   * @param date дата в формате yyyy-MM-dd
   * @return CompletableFuture с ID задачи
   * @throws IllegalStateException если возникла ошибка при работе с файловой системой
   */
  @Async
  public CompletableFuture<String> generateLogFile(String date) {
    String taskId = UUID.randomUUID().toString();
    LogTask task = new LogTask(taskId, "IN_PROGRESS", date, LocalDateTime.now());
    tasks.put(taskId, task);

    try {
      Files.createDirectories(Paths.get(LOG_DIR));
      String fileName = LOG_DIR + "log-" + date + "-" + System.currentTimeMillis() + ".log";

      try (var lines = Files.lines(Paths.get(SOURCE_LOG_PATH))) {
        Files.write(
                Paths.get(fileName),
                lines.filter(line -> line.contains(date)).collect(Collectors.toList()));
      }

      task.setStatus("COMPLETED");
      task.setFilePath(fileName);
    } catch (Exception e) {
      task.setStatus("FAILED");
      task.setErrorMessage(e.getMessage());
    }

    return CompletableFuture.completedFuture(taskId);
  }

  /**
   * Получает статус задачи по ID.
   *
   * @param taskId ID задачи
   * @return объект LogTask с информацией о задаче или null, если задача не найдена
   */
  public LogTask getTaskStatus(String taskId) {
    return tasks.get(taskId);
  }

  /**
   * Скачивает сгенерированный лог-файл.
   *
   * @param taskId ID задачи
   * @return ResponseEntity с файлом
   * @throws org.springframework.web.server.ResponseStatusException с кодом 404 если файл не найден
   */
  public ResponseEntity<Resource> downloadLogFile(String taskId) {
    LogTask task = tasks.get(taskId);
    if (task == null || !"COMPLETED".equals(task.getStatus())) {
      return ResponseEntity.notFound().build();
    }

    Resource resource = new FileSystemResource(task.getFilePath());
    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\""
                    + resource.getFilename() + "\"")
            .body(resource);
  }
}