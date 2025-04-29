package com.example.store.service;

import com.example.store.cache.ProductCache;
import com.example.store.model.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductCache productCache;

  @InjectMocks
  private ProductService productService;

  private Product createTestProduct(Long id, String name, int price, String category) {
    return Product.builder()
            .id(id)
            .name(name)
            .price(price)
            .category(category)
            .build();
  }

  @Test
  void getProducts_shouldReturnAllProducts_whenNoFilters() {
    Product product1 = createTestProduct(1L, "Product 1", 100, "Electronics");
    Product product2 = createTestProduct(2L, "Product 2", 200, "Books");
    when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

    List<Product> result = productService.getProducts(null, null);

    assertEquals(2, result.size());
    verify(productRepository, times(1)).findAll();
  }

  @Test
  void getProducts_shouldFilterByCategory() {
    Product product = createTestProduct(1L, "Product 1", 100, "Electronics");
    when(productRepository.findByCategory("Electronics")).thenReturn(List.of(product));

    List<Product> result = productService.getProducts("Electronics", null);

    assertEquals(1, result.size());
    assertEquals("Electronics", result.get(0).getCategory());
  }

  @Test
  void getProducts_shouldFilterByPrice() {
    Product product = createTestProduct(1L, "Product 1", 100, "Electronics");
    when(productRepository.findByPrice(100)).thenReturn(List.of(product));

    List<Product> result = productService.getProducts(null, 100);

    assertEquals(1, result.size());
    assertEquals(100, result.get(0).getPrice());
  }

  @Test
  void getProducts_shouldFilterByCategoryAndPrice() {
    Product product = createTestProduct(1L, "Product 1", 100, "Electronics");
    when(productRepository.findByCategoryAndPrice("Electronics", 100)).thenReturn(List.of(product));

    List<Product> result = productService.getProducts("Electronics", 100);

    assertEquals(1, result.size());
    assertEquals("Electronics", result.get(0).getCategory());
    assertEquals(100, result.get(0).getPrice());
  }

  @Test
  void getProductById_shouldReturnFromCache() {
    Long productId = 1L;
    Product cachedProduct = createTestProduct(productId, "Cached", 100, "Electronics");
    when(productCache.get(productId)).thenReturn(cachedProduct);

    Optional<Product> result = productService.getProductById(productId);

    assertTrue(result.isPresent());
    assertEquals("Cached", result.get().getName());
    verify(productRepository, never()).findById(any());
  }

  @Test
  void getProductById_shouldReturnFromDbAndCache() {
    Long productId = 1L;
    Product dbProduct = createTestProduct(productId, "DB Product", 100, "Electronics");
    when(productCache.get(productId)).thenReturn(null);
    when(productRepository.findById(productId)).thenReturn(Optional.of(dbProduct));

    Optional<Product> result = productService.getProductById(productId);

    assertTrue(result.isPresent());
    assertEquals("DB Product", result.get().getName());
    verify(productCache, times(1)).put(dbProduct);
  }

  @Test
  void getProductById_shouldReturnEmpty_whenProductNotFound() {
    Long productId = 99L;
    when(productCache.get(productId)).thenReturn(null);
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    Optional<Product> result = productService.getProductById(productId);

    assertTrue(result.isEmpty());
    verify(productCache, never()).put(any());
  }

  @Test
  void saveProduct_shouldSaveAndCache() {
    Product product = createTestProduct(null, "New Product", 100, "Electronics");
    Product savedProduct = createTestProduct(1L, "New Product", 100, "Electronics");
    when(productRepository.save(product)).thenReturn(savedProduct);

    Product result = productService.saveProduct(product);

    assertNotNull(result.getId());
    verify(productCache, times(1)).put(savedProduct);
  }

  @Test
  void deleteProduct_shouldRemoveFromOrdersAndDbAndCache() {
    Long productId = 1L;
    Product product = createTestProduct(productId, "To Delete", 100, "Electronics");
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    productService.deleteProduct(productId);

    verify(orderRepository, atLeastOnce()).findByProductsContaining(product);
    verify(productRepository, times(1)).delete(product);
    verify(productCache, times(1)).remove(productId);
  }

  @Test
  void deleteProduct_shouldThrowException_whenProductNotFound() {
    Long productId = 99L;
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class,
            () -> productService.deleteProduct(productId));
    assertEquals("Product not found", exception.getMessage());

    verify(productCache, never()).remove(any());
    verify(orderRepository, never()).findByProductsContaining(any());
  }
}