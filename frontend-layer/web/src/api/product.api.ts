import { http } from "./http.ts";

export interface Product {
  id: number;
  name: string;
  brand: string;
  description: string;
  price: number;
  stock: number;
}

export function getProducts(): Promise<Product[]> {
  return http<Product[]>("/products");
}