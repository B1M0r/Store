package com.example.store.service;

import com.example.store.model.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с аккаунтами пользователей.
 * Предоставляет методы для получения информации об аккаунтах.
 */
@Service
public class AccountService {
  private final List<Account> accounts = List.of(
          Account.builder().nickname("B1M0r").firstName("Daniil")
                  .lastName("Karasevich").email("tapelkabro@gmail.com").build(),
          Account.builder().nickname("user1").firstName("Pavel")
                  .lastName("Lasd").email("pasha@gmail.com").build()
  );

  // Метод для получения списка всех аккаунтов
  public List<Account> getAccounts() {
    return accounts;
  }

  /**
   * Возвращает аккаунт по указанному nickname.
   *
   * @param nickname никнейм аккаунта
   * @return Optional с аккаунтом, если он найден, или пустой Optional, если аккаунт не найден
   */
  public Optional<Account> getAccountByNickname(String nickname) {
    return accounts.stream()
            .filter(account -> account.getNickname().equals(nickname))
            .findFirst();
  }
}
