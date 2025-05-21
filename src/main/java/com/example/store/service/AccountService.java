package com.example.store.service;

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
   * Получить аккаунт по никнейму.
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
   * @throws RuntimeException если аккаунт не найден
   */
  @Transactional
  public void deleteAccount(Long id) {
    Account account = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    // Каскадное удаление заказов
    if (account.getOrders() != null) {
      account.getOrders().clear();
    }

    accountRepository.delete(account); // Удаляем аккаунт
  }

  @Transactional
  public Account updateAccount(Long id, Account updatedAccount) {
    Account existingAccount = accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));

    // Копируем только нужные поля (игнорируем заказы)
    existingAccount.setNickname(updatedAccount.getNickname());
    existingAccount.setFirstName(updatedAccount.getFirstName());
    existingAccount.setLastName(updatedAccount.getLastName());
    existingAccount.setEmail(updatedAccount.getEmail());

    return accountRepository.save(existingAccount); // Заказы останутся нетронутыми
  }
}