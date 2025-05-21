
import { Product, Account, Order } from '../types';

const API_URL = 'http://localhost:9090/api';

// Product API
export const fetchProducts = async (category?: string, price?: number): Promise<Product[]> => {
  let url = `${API_URL}/products`;
  const params = new URLSearchParams();
  
  if (category) params.append('category', category);
  if (price) params.append('price', price.toString());
  
  const queryString = params.toString();
  if (queryString) url += `?${queryString}`;
  
  const response = await fetch(url);
  if (!response.ok) throw new Error('Failed to fetch products');
  return response.json();
};

export const fetchProductById = async (id: number): Promise<Product> => {
  const response = await fetch(`${API_URL}/products/${id}`);
  if (!response.ok) throw new Error(`Failed to fetch product with id ${id}`);
  return response.json();
};

export const createProduct = async (product: Product): Promise<Product> => {
  const response = await fetch(`${API_URL}/products`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product)
  });
  if (!response.ok) throw new Error('Failed to create product');
  return response.json();
};

export const updateProduct = async (id: number, product: Product): Promise<Product> => {
  const response = await fetch(`${API_URL}/products/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product)
  });
  if (!response.ok) throw new Error(`Failed to update product with id ${id}`);
  return response.json();
};

export const deleteProduct = async (id: number): Promise<void> => {
  const response = await fetch(`${API_URL}/products/${id}`, { method: 'DELETE' });
  if (!response.ok) throw new Error(`Failed to delete product with id ${id}`);
};

// Account API
export const fetchAccounts = async (): Promise<Account[]> => {
  const response = await fetch(`${API_URL}/accounts`);
  if (!response.ok) throw new Error('Failed to fetch accounts');
  return response.json();
};

export const fetchAccountById = async (id: number): Promise<Account> => {
  const response = await fetch(`${API_URL}/accounts/${id}`);
  if (!response.ok) throw new Error(`Failed to fetch account with id ${id}`);
  return response.json();
};

export const createAccount = async (account: Account): Promise<Account> => {
  const response = await fetch(`${API_URL}/accounts`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(account)
  });
  if (!response.ok) throw new Error('Failed to create account');
  return response.json();
};

export const updateAccount = async (id: number, account: Account): Promise<Account> => {
  const response = await fetch(`${API_URL}/accounts/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(account)
  });
  if (!response.ok) throw new Error(`Failed to update account with id ${id}`);
  return response.json();
};

export const deleteAccount = async (id: number): Promise<void> => {
  const response = await fetch(`${API_URL}/accounts/${id}`, { method: 'DELETE' });
  if (!response.ok) throw new Error(`Failed to delete account with id ${id}`);
};

// Order API
export const fetchOrders = async (): Promise<Order[]> => {
  const response = await fetch(`${API_URL}/orders`);
  if (!response.ok) throw new Error('Failed to fetch orders');
  return response.json();
};

export const fetchOrderById = async (id: number): Promise<Order> => {
  const response = await fetch(`${API_URL}/orders/${id}`);
  if (!response.ok) throw new Error(`Failed to fetch order with id ${id}`);
  return response.json();
};

export const createOrder = async (order: Order): Promise<Order> => {
  const response = await fetch(`${API_URL}/orders`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(order)
  });
  if (!response.ok) throw new Error('Failed to create order');
  return response.json();
};

export const updateOrder = async (id: number, order: Order): Promise<Order> => {
  const response = await fetch(`${API_URL}/orders/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(order)
  });
  if (!response.ok) throw new Error(`Failed to update order with id ${id}`);
  return response.json();
};

export const deleteOrder = async (id: number): Promise<void> => {
  const response = await fetch(`${API_URL}/orders/${id}`, { method: 'DELETE' });
  if (!response.ok) throw new Error(`Failed to delete order with id ${id}`);
};
