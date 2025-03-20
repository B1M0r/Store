package com.example.store.service;

import com.example.store.cache.InMemoryCache;
import com.example.store.model.Account;
import com.example.store.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для управления сущностями {@link Account}.
 * Предоставляет методы для выполнения операций с аккаунтами.
 */
@Service
@AllArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final InMemoryCache cache; // Внедряем кэш

  /**
   * Получить все аккаунты.
   *
   * @return список всех аккаунтов
   */
  public List<Account> getAccounts() {
    String cacheKey = "all_accounts";

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return (List<Account>) cache.get(cacheKey);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Account> accounts = accountRepository.findAll();
    cache.put(cacheKey, accounts); // Сохраняем в кэш
    return accounts;
  }

  /**
   * Получить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  public Optional<Account> getAccountById(Long id) {
    String cacheKey = "account_" + id;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return Optional.of((Account) cache.get(cacheKey));
    }

    // Если данных нет в кэше, запрашиваем из базы
    Optional<Account> account = accountRepository.findById(id);
    account.ifPresent(acc -> cache.put(cacheKey, acc)); // Сохраняем в кэш
    return account;
  }

  /**
   * Получить аккаунт по никнейму.
   *
   * @param nickname никнейм аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  public Optional<Account> getAccountByNickname(String nickname) {
    String cacheKey = "account_nickname_" + nickname;

    // Проверяем кэш
    if (cache.containsKey(cacheKey)) {
      return Optional.of((Account) cache.get(cacheKey));
    }

    // Если данных нет в кэше, запрашиваем из базы
    Optional<Account> account = accountRepository.findByNickname(nickname);
    account.ifPresent(acc -> cache.put(cacheKey, acc)); // Сохраняем в кэш
    return account;
  }

  /**
   * Сохранить аккаунт (создание или обновление).
   *
   * @param account данные аккаунта
   * @return сохраненный аккаунт
   */
  public Account saveAccount(Account account) {
    Account savedAccount = accountRepository.save(account);
    cache.remove("all_accounts"); // Очищаем кэш для всех аккаунтов
    cache.remove("account_" + savedAccount.getId()); // Очищаем кэш для конкретного аккаунта
    cache.remove("account_nickname_"
            + savedAccount.getNickname()); // Очищаем кэш для аккаунта по никнейму
    return savedAccount;
  }

  /**
   * Удалить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   * @throws RuntimeException если аккаунт не найден
   */
  @Transactional
  public void deleteAccount(Long id) {
    Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    // Каскадное удаление заказов
    account.getOrders().clear(); // Удаляем все заказы, связанные с аккаунтом

    accountRepository.delete(account); // Удаляем аккаунт
    cache.remove("all_accounts"); // Очищаем кэш для всех аккаунтов
    cache.remove("account_" + id); // Очищаем кэш для конкретного аккаунта
    cache.remove("account_nickname_"
            + account.getNickname()); // Очищаем кэш для аккаунта по никнейму
  }
}