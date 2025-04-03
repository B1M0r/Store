package com.example.store.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность товара в системе.
 * Содержит информацию о названии, цене, категории товара,
 * а также связи с аккаунтом и заказами.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  /**
   * Уникальный идентификатор товара.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Название товара.
   * Не может быть пустым, максимальная длина - 255 символов.
   */
  @Column(nullable = false)
  @NotBlank(message = "Название товара обязательно")
  @Size(max = 255, message = "Название не должно превышать 255 символов")
  private String name;

  /**
   * Цена товара.
   * Должна быть положительным числом.
   */
  @Column(nullable = false)
  @Positive(message = "Цена должна быть положительным числом")
  private int price;

  /**
   * Категория товара.
   * Не может быть пустой, максимальная длина - 100 символов.
   */
  @Column(nullable = false)
  @NotBlank(message = "Категория товара обязательна")
  @Size(max = 100, message = "Категория не должна превышать 100 символов")
  private String category;

  /**
   * Аккаунт, которому принадлежит товар.
   * Игнорируются поля products и orders при сериализации.
   */
  @ManyToOne
  @JoinColumn(name = "account_id")
  @JsonIgnoreProperties({"products", "orders"})
  private Account account;

  /**
   * Список заказов, содержащих данный товар.
   * Игнорируются поля account и products при сериализации.
   */
  @ManyToMany(mappedBy = "products")
  @JsonIgnoreProperties({"account", "products"})
  private List<Order> orders;
}