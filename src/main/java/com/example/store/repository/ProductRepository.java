package com.example.store.repository;

import com.example.store.model.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для управления сущностями {@link Product}.
 * Предоставляет методы для выполнения операций с продуктами в базе данных.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}