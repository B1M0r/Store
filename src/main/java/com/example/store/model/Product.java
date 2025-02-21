package com.example.store.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс Product представляет товар в магазине.
 * Содержит информацию о названии товара, его идентификаторе, цене и категории.
 */
@Getter
@Setter
@Builder
public class Product {
  private String name;
  private int id;
  private int price;
  private String category;
}
