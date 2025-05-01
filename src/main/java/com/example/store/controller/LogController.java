package com.example.store.controller;

import com.example.store.model.LogTask;
import com.example.store.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с лог-файлами приложения.
 *
 * <p>Предоставляет API для:
 * <ul>
 *   <li>Асинхронной генерации лог-файлов
 *   <li>Проверки статуса задач
 *   <li>Скачивания сгенерированных файлов
 *   <li>Просмотра логов по дате
 * </ul>
 */
@RestController
@RequestMapping("/api/logs")
@Tag(name = "Log Controller", description = "API для работы с лог-файлами")
public class LogController {

  private static final String LOG_FILE_PATH = "./store.log";
  private static final DateTimeFormatter DATE_FORMATTER =
          DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final LogService logService;

  /**
   * Создает новый экземпляр LogController с указанным сервисом работы с логами.
   *
   * @param logService сервис для операций с лог-файлами
   */
  public LogController(LogService logService) {
    this.logService = logService;
  }

  /**
   * Асинхронно генерирует лог-файл для указанной даты.
   *
   * @param date дата в формате yyyy-MM-dd
   * @return Map с ID задачи в формате {"taskId": "string"}
   */
  @PostMapping("/generate")
  @Operation(
          summary = "Создать лог-файл",
          description = "Асинхронно создает лог-файл за указанную дату")
  @ApiResponse(responseCode = "200", description = "Задача на генерацию создана")
  public Map<String, String> generateLogFile(
          @Parameter(description = "Дата в формате yyyy-MM-dd", example = "2023-10-01")
          @RequestParam String date) {
    String taskId = logService.generateLogFile(date).join();
    return Map.of("taskId", taskId);
  }

  /**
   * Проверяет статус задачи генерации логов.
   *
   * @param taskId ID задачи
   * @return объект LogTask с информацией о задаче
   */
  @GetMapping("/status/{taskId}")
  @Operation(
          summary = "Проверить статус задачи",
          description = "Возвращает статус задачи по её ID")
  @ApiResponse(responseCode = "200", description = "Статус задачи получен")
  @ApiResponse(responseCode = "404", description = "Задача не найдена")
  public LogTask getTaskStatus(
          @Parameter(description = "ID задачи", example = "550e8400-e29b-41d4-a716-446655440000")
          @PathVariable String taskId) {
    return logService.getTaskStatus(taskId);
  }

  /**
   * Скачивает сгенерированный лог-файл.
   *
   * @param taskId ID задачи
   * @return ResponseEntity с файлом или сообщением об ошибке
   */
  @GetMapping("/download/{taskId}")
  @Operation(
          summary = "Скачать лог-файл",
          description = "Скачивает сгенерированный лог-файл по ID задачи")
  @ApiResponse(responseCode = "200", description = "Файл успешно скачан")
  @ApiResponse(responseCode = "404", description = "Файл не найден или задача не завершена")
  public ResponseEntity<?> downloadLogFile(
          @Parameter(description = "ID задачи", example = "550e8400-e29b-41d4-a716-446655440000")
          @PathVariable String taskId) {
    return logService.downloadLogFile(taskId);
  }

  /**
   * Возвращает логи за указанную дату.
   *
   * @param date дата в формате yyyy-MM-dd
   * @return ResponseEntity с отфильтрованными логами или сообщением об ошибке
   */
  @GetMapping("/by-date")
  @Operation(
          summary = "Получить логи за дату",
          description = "Возвращает логи за указанную дату")
  @ApiResponse(responseCode = "200", description = "Логи успешно получены")
  @ApiResponse(responseCode = "400", description = "Неверный формат даты")
  @ApiResponse(responseCode = "404", description = "Логи не найдены")
  public ResponseEntity<String> getLogsByDate(
          @Parameter(description = "Дата в формате yyyy-MM-dd", example = "2023-10-01")
          @RequestParam String date) {

    try {
      LocalDate.parse(date, DATE_FORMATTER);
    } catch (DateTimeParseException e) {
      return ResponseEntity.badRequest()
              .body("Неверный формат даты. Используйте формат yyyy-MM-dd");
    }

    try {
      Path logPath = Paths.get(LOG_FILE_PATH);
      if (!Files.exists(logPath)) {
        return ResponseEntity.notFound().build();
      }

      String logs;
      try (Stream<String> lines = Files.lines(logPath)) {
        logs = lines
                .filter(line -> line.contains(date))
                .collect(Collectors.joining("\n"));
      }

      if (logs.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok()
              .contentType(MediaType.TEXT_PLAIN)
              .body(logs);
    } catch (IOException e) {
      return ResponseEntity.internalServerError()
              .body("Ошибка при чтении лог-файла");
    }
  }
}