import React, { useEffect, useState } from "react";
import { getGuestCart, type CartResponse } from "../api/cart.api.ts";
import CartItem from "../components/CartItem";

export default function Cart() {
  const [cart, setCart] = useState<CartResponse | null>(null);

  useEffect(() => {
    getGuestCart()
      .then(setCart)
      .catch(console.error);
  }, []);

  const handleRemove = (productId: number) => {
    setCart(prev => prev ? {
      ...prev,
      items: prev.items.filter(item => item.productId !== productId)
    } : null);
  };

  const handleQuantityChange = (productId: number, quantity: number) => {
    setCart(prev => prev ? {
      ...prev,
      items: prev.items.map(item =>
        item.productId === productId ? { ...item, quantity } : item
      )
    } : null);
  };

  if (!cart) return <p>Loading cart...</p>;

  return (
    <div>
      <h1>Shopping Cart</h1>
      {cart.items.length === 0 ? (
        <p>Your cart is empty.</p>
      ) : (
        cart.items.map(item => (
          <CartItem
            key={item.productId}
            item={item}
            onRemove={handleRemove}
            onQuantityChange={handleQuantityChange}
          />
        ))
      )}
      <h2>Total: ${cart.items.reduce((sum, item) => sum + item.price * item.quantity, 0)}</h2>
    </div>
  );
}
