package com.example.store.controller;

import com.example.store.model.Account;
import com.example.store.service.AccountService;
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
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для управления аккаунтами.
 * Предоставляет REST API для выполнения CRUD-операций с сущностью {@link Account}.
 */
@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

  private final AccountService accountService;

  /**
   * Получить все аккаунты.
   *
   * @return список всех аккаунтов
   */
  @GetMapping
  public List<Account> getAllAccounts() {
    return accountService.getAccounts();
  }

  /**
   * Получить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   * @return аккаунт с указанным ID
   * @throws ResponseStatusException если аккаунт не найден
   */
  @GetMapping("/{id}")
  public Account getAccountById(@PathVariable Long id) {
    return accountService.getAccountById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account not found"));
  }

  /**
   * Получить аккаунт по nickname.
   *
   * @param nickname никнейм аккаунта
   * @return аккаунт с указанным nickname
   * @throws ResponseStatusException если аккаунт не найден
   */
  @GetMapping("/nickname/{nickname}")
  public Account getAccountByNickname(@PathVariable String nickname) {
    return accountService.getAccountByNickname(nickname)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account not found"));
  }

  /**
   * Создать новый аккаунт.
   *
   * @param account данные аккаунта
   * @return созданный аккаунт
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Account createAccount(@RequestBody Account account) {
    return accountService.saveAccount(account);
  }

  /**
   * Обновить существующий аккаунт.
   *
   * @param id идентификатор аккаунта
   * @param account новые данные аккаунта
   * @return обновленный аккаунт
   */
  @PutMapping("/{id}")
  public Account updateAccount(@PathVariable Long id, @RequestBody Account account) {
    account.setId(id);
    return accountService.saveAccount(account);
  }

  /**
   * Удалить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAccount(@PathVariable Long id) {
    accountService.deleteAccount(id);
  }
}