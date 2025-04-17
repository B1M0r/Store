package com.example.store.controller;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.model.Account;
import com.example.store.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

  @Mock
  private AccountService accountService;

  @InjectMocks
  private AccountController accountController;

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
  void getAllAccounts_shouldReturnAccountsList() {
    Account account = createTestAccount(1L, "test", "Test", "User", "test@test.com");
    when(accountService.getAccounts()).thenReturn(Collections.singletonList(account));

    ResponseEntity<List<Account>> response = accountController.getAllAccounts();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("test", response.getBody().get(0).getNickname());
  }

  @Test
  void getAccountById_shouldReturnAccountWhenExists() {
    Long accountId = 1L;
    Account mockAccount = createTestAccount(accountId, "test", "Test", "User", "test@test.com");
    when(accountService.getAccountById(accountId)).thenReturn(Optional.of(mockAccount));

    ResponseEntity<Account> response = accountController.getAccountById(accountId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(accountId, response.getBody().getId());
  }

  @Test
  void getAccountById_shouldThrowNotFoundWhenNotExists() {
    when(accountService.getAccountById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> accountController.getAccountById(99L));
  }

  @Test
  void createAccount_shouldReturnCreatedAccount() {
    Account newAccount = createTestAccount(null, "new", "New", "User", "new@test.com");
    Account savedAccount = createTestAccount(1L, "new", "New", "User", "new@test.com");
    when(accountService.saveAccount(newAccount)).thenReturn(savedAccount);

    ResponseEntity<Account> response = accountController.createAccount(newAccount);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody().getId());
  }

  @Test
  void updateAccount_shouldReturnUpdatedAccount() {
    Long accountId = 1L;
    Account updatedAccount = createTestAccount(accountId, "updated", "Updated", "User", "updated@test.com");
    when(accountService.saveAccount(updatedAccount)).thenReturn(updatedAccount);

    ResponseEntity<Account> response = accountController.updateAccount(accountId, updatedAccount);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("updated", response.getBody().getNickname());
  }

  @Test
  void deleteAccount_shouldReturnNoContent() {
    Long accountId = 1L;
    doNothing().when(accountService).deleteAccount(accountId);

    ResponseEntity<Void> response = accountController.deleteAccount(accountId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(accountService, times(1)).deleteAccount(accountId);
  }
}