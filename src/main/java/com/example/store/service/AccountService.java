package com.example.store.service;

import com.example.store.model.Account;
import com.example.store.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления сущностями {@link Account}.
 * Предоставляет методы для выполнения операций с аккаунтами.
 */
@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  /**
   * Получить все аккаунты.
   *
   * @return список всех аккаунтов
   */
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  /**
   * Получить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  public Optional<Account> getAccountById(Long id) {
    return accountRepository.findById(id);
  }

  /**
   * Получить аккаунт по nickname.
   *
   * @param nickname никнейм аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  public Optional<Account> getAccountByNickname(String nickname) {
    return accountRepository.findByNickname(nickname);
  }

  /**
   * Сохранить аккаунт (создание или обновление).
   *
   * @param account данные аккаунта
   * @return сохраненный аккаунт
   */
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  /**
   * Удалить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   */
  public void deleteAccount(Long id) {
    accountRepository.deleteById(id);
  }
}