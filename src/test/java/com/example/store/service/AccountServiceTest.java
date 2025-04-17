package com.example.store.service;

import com.example.store.model.Account;
import com.example.store.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountService accountService;

  private Account createTestAccount(Long id, String nickname, String firstName, String lastName, String email) {
    return Account.builder()
            .id(id)
            .nickname(nickname)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .build();
  }

  @Test
  void getAccounts_shouldReturnAllAccounts() {
    Account account1 = createTestAccount(1L, "user1", "John", "Doe", "john@test.com");
    Account account2 = createTestAccount(2L, "user2", "Jane", "Smith", "jane@test.com");
    when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

    List<Account> result = accountService.getAccounts();

    assertEquals(2, result.size());
    assertEquals("user1", result.get(0).getNickname());
    verify(accountRepository, times(1)).findAll();
  }

  @Test
  void getAccountById_shouldReturnAccountWhenExists() {
    Long accountId = 1L;
    Account mockAccount = createTestAccount(accountId, "test", "Test", "User", "test@test.com");
    when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

    Optional<Account> result = accountService.getAccountById(accountId);

    assertTrue(result.isPresent());
    assertEquals(accountId, result.get().getId());
  }

  @Test
  void getAccountById_shouldReturnEmptyWhenNotExists() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

    Optional<Account> result = accountService.getAccountById(99L);

    assertTrue(result.isEmpty());
  }

  @Test
  void saveAccount_shouldReturnSavedAccount() {
    Account newAccount = createTestAccount(null, "new", "New", "User", "new@test.com");
    Account savedAccount = createTestAccount(1L, "new", "New", "User", "new@test.com");
    when(accountRepository.save(newAccount)).thenReturn(savedAccount);

    Account result = accountService.saveAccount(newAccount);

    assertNotNull(result.getId());
    assertEquals(savedAccount.getNickname(), result.getNickname());
  }

  @Test
  void deleteAccount_shouldDeleteWhenAccountExists() {
    Long accountId = 1L;
    Account mockAccount = createTestAccount(accountId, "test", "Test", "User", "test@test.com");
    when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

    assertDoesNotThrow(() -> accountService.deleteAccount(accountId));
    verify(accountRepository, times(1)).delete(mockAccount);
  }

  @Test
  void deleteAccount_shouldThrowExceptionWhenAccountNotExists() {
    when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> accountService.deleteAccount(99L));
    verify(accountRepository, never()).delete(any());
  }
}