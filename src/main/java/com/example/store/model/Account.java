package com.example.store.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Класс Account представляет аккаунт пользователя в системе.
 * Содержит основную информацию о пользователе, такую как никнейм, имя, фамилия и email.
 */
@Getter
@Setter
@Builder
public class Account {
  private String nickname;
  private String firstName;
  private String lastName;
  private String email;
}
