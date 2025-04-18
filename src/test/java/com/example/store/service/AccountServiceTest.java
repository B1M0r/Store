package com.example.store.service;

import com.example.store.model.Account;
import com.example.store.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountService accountService;

  private AutoCloseable closeable;

  private Account mockAccount;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);

    mockAccount = Account.builder()
            .id(1L)
            .nickname("testuser")
            .firstName("Test")
            .lastName("User")
            .email("test@example.com")
            .build();
  }

  @Test
  void testGetAccounts() {
    List<Account> accounts = List.of(mockAccount);
    when(accountRepository.findAll()).thenReturn(accounts);

    List<Account> result = accountService.getAccounts();

    assertEquals(1, result.size());
    assertEquals("testuser", result.get(0).getNickname());
  }

  @Test
  void testGetAccountById_found() {
    when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

    Optional<Account> result = accountService.getAccountById(1L);

    assertTrue(result.isPresent());
    assertEquals("testuser", result.get().getNickname());
  }

  @Test
  void testGetAccountById_notFound() {
    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

    Optional<Account> result = accountService.getAccountById(1L);

    assertFalse(result.isPresent());
  }

  @Test
  void testSaveAccount() {
    when(accountRepository.save(mockAccount)).thenReturn(mockAccount);

    Account saved = accountService.saveAccount(mockAccount);

    assertNotNull(saved);
    assertEquals("testuser", saved.getNickname());
  }

  @Test
  void testDeleteAccount_success() {
    when(accountRepository.findById(1L)).thenReturn(Optional.of(mockAccount));

    // simulate clearing orders
    mockAccount.setOrders(new ArrayList<>());

    accountService.deleteAccount(1L);

    verify(accountRepository, times(1)).delete(mockAccount);
  }

  @Test
  void testDeleteAccount_notFound() {
    when(accountRepository.findById(1L)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class,
            () -> accountService.deleteAccount(1L));

    assertEquals("Account not found", exception.getMessage());
  }

  @Test
  void testGetAccountByNickname() {
    when(accountRepository.findByNickname("testuser")).thenReturn(Optional.of(mockAccount));

    Optional<Account> result = accountService.getAccountByNickname("testuser");

    assertTrue(result.isPresent());
    assertEquals("test@example.com", result.get().getEmail());
  }
}
