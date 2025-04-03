package com.example.store.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность заказа в системе.
 * Содержит информацию о дате заказа, общей стоимости, связанном аккаунте и продуктах.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Дата и время оформления заказа.
   * Не может быть null.
   */
  @Column(nullable = false)
  @NotNull(message = "Дата заказа обязательна для заполнения")
  private LocalDateTime orderDate;

  /**
   * Общая стоимость заказа.
   * Должна быть положительной или нулевой.
   */
  @Column(nullable = false)
  @PositiveOrZero(message = "Общая стоимость должна быть положительной или нулевой")
  private double totalPrice;

  /**
   * Аккаунт, к которому относится заказ.
   * Не может быть null.
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id", nullable = false)
  @JsonBackReference
  @NotNull(message = "Аккаунт обязателен для заполнения")
  private Account account;

  /**
   * Список продуктов в заказе.
   * Связь многие-ко-многим с промежуточной таблицей.
   */
  @ManyToMany(
          cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          fetch = FetchType.LAZY)
  @JoinTable(
          name = "order_product",
          joinColumns = @JoinColumn(name = "order_id"),
          inverseJoinColumns = @JoinColumn(name = "product_id"))
  @JsonIgnoreProperties({"orders", "account"})
  private List<Product> products;

  /**
   * Временное поле для передачи ID продуктов.
   * Не сохраняется в базе данных.
   */
  @Transient
  @JsonProperty("productIds")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<Long> productIds;
}