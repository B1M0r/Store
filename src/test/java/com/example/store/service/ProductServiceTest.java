package com.example.store.service;

import com.example.store.cache.ProductCache;
import com.example.store.model.Product;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

  private ProductRepository productRepository;
  private OrderRepository orderRepository;
  private ProductCache productCache;
  private ProductService productService;

  @BeforeEach
  void setUp() {
    productRepository = mock(ProductRepository.class);
    orderRepository = mock(OrderRepository.class);
    productCache = mock(ProductCache.class);
    productService = new ProductService(productRepository, orderRepository, productCache);
  }

  @Test
  void testGetProducts_NoFilter_ReturnsAll() {
    List<Product> expected = List.of(new Product());
    when(productRepository.findAll()).thenReturn(expected);

    List<Product> result = productService.getProducts(null, null);

    assertEquals(expected, result);
  }

  @Test
  void testGetProducts_ByCategory() {
    String category = "Books";
    List<Product> expected = List.of(new Product());
    when(productRepository.findByCategory(category)).thenReturn(expected);

    List<Product> result = productService.getProducts(category, null);

    assertEquals(expected, result);
  }

  @Test
  void testGetProductById_FromCache() {
    Product product = new Product();
    product.setId(1L);
    when(productCache.get(1L)).thenReturn(product);

    Optional<Product> result = productService.getProductById(1L);

    assertTrue(result.isPresent());
    assertEquals(product, result.get());
    verify(productRepository, never()).findById(any());
  }

  @Test
  void testGetProductById_FromRepositoryAndCacheUpdated() {
    Product product = new Product();
    product.setId(2L);
    when(productCache.get(2L)).thenReturn(null);
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));

    Optional<Product> result = productService.getProductById(2L);

    assertTrue(result.isPresent());
    assertEquals(product, result.get());
    verify(productCache).put(product);
  }

  @Test
  void testSaveProduct_UpdatesCache() {
    Product product = new Product();
    product.setId(3L);
    when(productRepository.save(product)).thenReturn(product);

    Product result = productService.saveProduct(product);

    assertEquals(product, result);
    verify(productCache).put(product);
  }

  @Test
  void testDeleteProduct_RemovesFromOrdersAndCache() {
    Product product = new Product();
    product.setId(4L);
    when(productRepository.findById(4L)).thenReturn(Optional.of(product));
    when(orderRepository.findByProductsContaining(product)).thenReturn(Collections.emptyList());

    productService.deleteProduct(4L);

    verify(orderRepository).findByProductsContaining(product);
    verify(productRepository).delete(product);
    verify(productCache).remove(4L);
  }

  @Test
  void testDeleteProduct_NotFound_Throws() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class,
            () -> productService.deleteProduct(999L));

    assertEquals("Product not found", exception.getMessage());
  }
}
