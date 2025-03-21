package com.example.store.repository;

import com.example.store.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для управления сущностями {@link Account}.
 * Предоставляет методы для выполнения операций с аккаунтами в базе данных.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

  /**
   * Найти аккаунт по никнейму.
   *
   * @param nickname никнейм аккаунта
   * @return Optional, содержащий аккаунт, если он найден
   */
  Optional<Account> findByNickname(String nickname);
}
