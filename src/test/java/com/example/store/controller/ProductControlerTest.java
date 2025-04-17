package com.example.store.controller;

import com.example.store.exception.ResourceNotFoundException;
import com.example.store.model.Product;
import com.example.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  private ProductController productController;

  private Product createTestProduct(Long id, String name, int price, String category) {
    return Product.builder()
            .id(id)
            .name(name)
            .price(price)
            .category(category)
            .build();
  }

  @Test
  void getProducts_shouldReturnFilteredProducts() {
    Product product = createTestProduct(1L, "Laptop", 1000, "Electronics");
    when(productService.getProducts("Electronics", null)).thenReturn(List.of(product));

    ResponseEntity<List<Product>> response = productController.getProducts("Electronics", null);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    assertEquals("Laptop", response.getBody().get(0).getName());
  }

  @Test
  void getProducts_shouldThrowNotFoundWhenEmpty() {
    when(productService.getProducts("Unknown", null)).thenReturn(List.of());

    assertThrows(ResourceNotFoundException.class,
            () -> productController.getProducts("Unknown", null));
  }

  @Test
  void getProductById_shouldReturnProduct() {
    Product product = createTestProduct(1L, "Laptop", 1000, "Electronics");
    when(productService.getProductById(1L)).thenReturn(Optional.of(product));

    ResponseEntity<Product> response = productController.getProductById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Laptop", response.getBody().getName());
  }

  @Test
  void getProductById_shouldThrowNotFound() {
    when(productService.getProductById(99L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class,
            () -> productController.getProductById(99L));
  }

  @Test
  void createProduct_shouldReturnCreatedProduct() {
    Product newProduct = createTestProduct(null, "New", 100, "Category");
    Product savedProduct = createTestProduct(1L, "New", 100, "Category");
    when(productService.saveProduct(newProduct)).thenReturn(savedProduct);

    ResponseEntity<Product> response = productController.createProduct(newProduct);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody().getId());
  }

  @Test
  void updateProduct_shouldReturnUpdatedProduct() {
    Product updatedProduct = createTestProduct(1L, "Updated", 200, "Category");
    when(productService.saveProduct(updatedProduct)).thenReturn(updatedProduct);

    ResponseEntity<Product> response = productController.updateProduct(1L, updatedProduct);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Updated", response.getBody().getName());
  }

  @Test
  void deleteProduct_shouldReturnNoContent() {
    doNothing().when(productService).deleteProduct(1L);

    ResponseEntity<Void> response = productController.deleteProduct(1L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(productService, times(1)).deleteProduct(1L);
  }

  @Test
  void createProductsBulk_shouldReturnCreatedProducts() {
    Product product1 = createTestProduct(null, "Product 1", 100, "Category");
    Product product2 = createTestProduct(null, "Product 2", 200, "Category");
    List<Product> newProducts = List.of(product1, product2);

    Product savedProduct1 = createTestProduct(1L, "Product 1", 100, "Category");
    Product savedProduct2 = createTestProduct(2L, "Product 2", 200, "Category");
    when(productService.saveProduct(product1)).thenReturn(savedProduct1);
    when(productService.saveProduct(product2)).thenReturn(savedProduct2);

    ResponseEntity<List<Product>> response = productController.createProductsBulk(newProducts);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(2, response.getBody().size());
    assertEquals(1L, response.getBody().get(0).getId());
  }
}