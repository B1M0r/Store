package com.example.store.controller;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.model.Account;
import com.example.store.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления учетными записями пользователей.
 * Предоставляет REST API для выполнения операций CRUD с учетными записями.
 */
@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Account Controller", description = "API для управления аккаунтами")
public class AccountController {

  private final AccountService accountService;

  /**
   * Получает список всех учетных записей.
   *
   * @return ResponseEntity со списком всех учетных записей
   */
  @GetMapping
  @Operation(
          summary = "Получить все аккаунты",
          description = "Возвращает список всех аккаунтов")
  @ApiResponse(
          responseCode = "200",
          description = "Успешный запрос",
          content = @Content(schema = @Schema(implementation = Account.class)))
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountService.getAccounts());
  }

  /**
   * Получает учетную запись по идентификатору.
   *
   * @param id идентификатор учетной записи
   * @return ResponseEntity с найденной учетной записью
   * @throws ResourceNotFoundException если учетная запись не найдена
   */
  @GetMapping("/{id}")
  @Operation(
          summary = "Получить аккаунт по ID",
          description = "Возвращает аккаунт по указанному ID")
  @ApiResponse(
          responseCode = "200",
          description = "Аккаунт найден",
          content = @Content(schema = @Schema(implementation = Account.class)))
  @ApiResponse(
          responseCode = "404",
          description = "Аккаунт не найден")
  public ResponseEntity<Account> getAccountById(
          @Parameter(
                  description = "ID аккаунта",
                  example = "1",
                  required = true)
          @PathVariable Long id) {
    return ResponseEntity.ok(accountService.getAccountById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id)));
  }

  /**
   * Создает новую учетную запись.
   *
   * @param account данные новой учетной записи
   * @return ResponseEntity с созданной учетной записью
   */
  @PostMapping
  @Operation(
          summary = "Создать аккаунт",
          description = "Создает новый аккаунт")
  @ApiResponse(
          responseCode = "201",
          description = "Аккаунт создан",
          content = @Content(schema = @Schema(implementation = Account.class)))
  @ApiResponse(
          responseCode = "400",
          description = "Некорректные данные")
  public ResponseEntity<Account> createAccount(
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Данные аккаунта",
                  required = true,
                  content = @Content(schema = @Schema(implementation = Account.class)))
          @Valid @RequestBody Account account) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(accountService.saveAccount(account));
  }

  /**
   * Обновляет существующую учетную запись.
   *
   * @param id идентификатор обновляемой учетной записи
   * @param account новые данные учетной записи
   * @return ResponseEntity с обновленной учетной записью
   */
  @PutMapping("/{id}")
  @Operation(
          summary = "Обновить аккаунт",
          description = "Обновляет существующий аккаунт")
  @ApiResponse(
          responseCode = "200",
          description = "Аккаунт обновлен",
          content = @Content(schema = @Schema(implementation = Account.class)))
  @ApiResponse(
          responseCode = "400",
          description = "Некорректные данные")
  @ApiResponse(
          responseCode = "404",
          description = "Аккаунт не найден")
  public ResponseEntity<Account> updateAccount(
          @Parameter(
                  description = "ID аккаунта",
                  example = "1",
                  required = true)
          @PathVariable Long id,
          @Valid @RequestBody Account account) {
    account.setId(id);
    return ResponseEntity.ok(accountService.saveAccount(account));
  }

  /**
   * Удаляет учетную запись по идентификатору.
   *
   * @param id идентификатор удаляемой учетной записи
   * @return ResponseEntity без содержимого
   */
  @DeleteMapping("/{id}")
  @Operation(
          summary = "Удалить аккаунт",
          description = "Удаляет аккаунт по ID")
  @ApiResponse(
          responseCode = "204",
          description = "Аккаунт удален")
  @ApiResponse(
          responseCode = "404",
          description = "Аккаунт не найден")
  public ResponseEntity<Void> deleteAccount(
          @Parameter(
                  description = "ID аккаунта",
                  example = "1",
                  required = true)
          @PathVariable Long id) {
    accountService.deleteAccount(id);
    return ResponseEntity.noContent().build();
  }
}