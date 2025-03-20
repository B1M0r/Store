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

  private static final String CACHE_KEY_ALL_ACCOUNTS = "all_accounts";
  private static final String CACHE_KEY_ACCOUNT_PREFIX = "account_";
  private static final String CACHE_KEY_ACCOUNT_NICKNAME_PREFIX = "account_nickname_";

  private final AccountRepository accountRepository;
  private final InMemoryCache cache; // Внедряем кэш

  /**
   * Получить все аккаунты.
   *
   * @return список всех аккаунтов
   */
  public List<Account> getAccounts() {
    // Проверяем кэш
    if (cache.containsKey(CACHE_KEY_ALL_ACCOUNTS)) {
      return (List<Account>) cache.get(CACHE_KEY_ALL_ACCOUNTS);
    }

    // Если данных нет в кэше, запрашиваем из базы
    List<Account> accounts = accountRepository.findAll();
    cache.put(CACHE_KEY_ALL_ACCOUNTS, accounts); // Сохраняем в кэш
    return accounts;
  }

  /**
   * Получить аккаунт по ID.
   *
   * @param id идентификатор аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  public Optional<Account> getAccountById(Long id) {
    String cacheKey = CACHE_KEY_ACCOUNT_PREFIX + id;

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
    String cacheKey = CACHE_KEY_ACCOUNT_NICKNAME_PREFIX + nickname;

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
    cache.remove(CACHE_KEY_ALL_ACCOUNTS); // Очищаем кэш для всех аккаунтов
    cache.remove(CACHE_KEY_ACCOUNT_PREFIX + savedAccount.getId()); // Очищаем кэш для конкретного аккаунта
    cache.remove(CACHE_KEY_ACCOUNT_NICKNAME_PREFIX + savedAccount.getNickname()); // Очищаем кэш для аккаунта по никнейму
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
    cache.remove(CACHE_KEY_ALL_ACCOUNTS); // Очищаем кэш для всех аккаунтов
    cache.remove(CACHE_KEY_ACCOUNT_PREFIX + id); // Очищаем кэш для конкретного аккаунта
    cache.remove(CACHE_KEY_ACCOUNT_NICKNAME_PREFIX + account.getNickname()); // Очищаем кэш для аккаунта по никнейму
  }
}