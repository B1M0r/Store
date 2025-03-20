package com.example.store.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность, представляющая заказ.
 * Заказ содержит информацию о дате заказа, общей стоимости, аккаунте и продуктах.
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

  @Column(nullable = false)
  private LocalDateTime orderDate;

  @Column(nullable = false)
  private double totalPrice;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "account_id", nullable = false)
  @JsonBackReference
  private Account account;

  @ManyToMany(
          cascade = {CascadeType.PERSIST, CascadeType.MERGE},
          fetch = FetchType.LAZY)
  @JoinTable(
          name = "order_product",
          joinColumns = @JoinColumn(name = "order_id"),
          inverseJoinColumns = @JoinColumn(name = "product_id"))
  @JsonIgnoreProperties({"orders", "account"}) // Игнорируем зацикливание
  private List<Product> products;

  private List<Long> productIds;
}