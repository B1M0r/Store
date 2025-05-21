
export interface Product {
  id?: number;
  name: string;
  price: number;
  category: string;
  account?: Account;
  orders?: Order[];
}

export interface Account {
  id?: number;
  nickname: string;
  firstName: string;
  lastName: string;
  email: string;
  orders?: Order[];
  products?: Product[];
}

export interface Order {
  id?: number;
  orderDate: string;
  totalPrice: number;
  account?: Account;
  products?: Product[];
  productIds?: number[];
}
