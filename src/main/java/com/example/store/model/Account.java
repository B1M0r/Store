package com.example.store.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность, представляющая аккаунт пользователя.
 * Аккаунт содержит информацию о пользователе, такую как никнейм, имя, фамилия и email.
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Nickname is required")
  @Size(max = 50, message = "Nickname must be less than 50 characters")
  private String nickname;

  @Column(nullable = false)
  @NotBlank(message = "First name is required")
  @Size(max = 100, message = "First name must be less than 100 characters")
  private String firstName;

  @Column(nullable = false)
  @NotBlank(message = "Last name is required")
  @Size(max = 100, message = "Last name must be less than 100 characters")
  private String lastName;

  @Column(unique = true, nullable = false)
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,
          fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonManagedReference
  private List<Order> orders;

  @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Product> products;
}