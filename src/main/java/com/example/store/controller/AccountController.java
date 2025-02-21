package com.example.store.controller;

import com.example.store.model.Account;
import com.example.store.service.AccountService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер для управления аккаунтами пользователей.
 * Предоставляет REST-эндпоинты для получения информации об аккаунтах.
 */
@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  public List<Account> getAccounts() {
    return accountService.getAccounts();
  }

  /**
   * Возвращает аккаунт по указанному nickname.
   *
   * @param nickname никнейм аккаунта
   * @return ResponseEntity с аккаунтом, если он найден, или статус 404, если аккаунт не найден
   */
  @GetMapping("/{nickname}")
  public Account getAccountByNickname(@PathVariable("nickname") String nickname) {
    return accountService.getAccountByNickname(nickname)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Account not found"));
  }
}
