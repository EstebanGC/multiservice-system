import { useEffect, useState } from "react";
import { getProducts } from "../api/product.api";
import type { Product } from "../api/product.api";
import { addToCartGuest } from "../api/cart.api.ts";

export default function Products() {
  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    getProducts().then(setProducts).catch(console.error);
  }, []);

  return (
    <div>
      <h1>Products</h1>

      {products.map(p => (
        <div key={p.id}>
          <h3>{p.name}</h3>
          <p>${p.price}</p>
          <button onClick={() => addToCartGuest({ productId: p.id, quantity: 1 })}>
            Add to cart
          </button>
        </div>
      ))}
    </div>
  );
}