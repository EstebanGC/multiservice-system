import type { CartItem } from "../api/cart.api.ts";

interface CartItemProps {
  item: CartItem;
  onRemove: (productId: number) => void;
  onQuantityChange: (productId: number, quantity: number) => void;
}

export default function CartItem({ item, onRemove, onQuantityChange }: CartItemProps) {
  return (
    <div style={{ border: "1px solid #ccc", padding: "10px", marginBottom: "10px" }}>
      <h3>{item.productName}</h3>
      <p>Price: ${item.price}</p>
      <p>
        Quantity: 
        <button onClick={() => onQuantityChange(item.productId, item.quantity - 1)} disabled={item.quantity <= 1}>-</button>
        {item.quantity}
        <button onClick={() => onQuantityChange(item.productId, item.quantity + 1)}>+</button>
      </p>
      <p>Total: ${item.price * item.quantity}</p>
      <button onClick={() => onRemove(item.productId)}>Remove</button>
    </div>
  );
}
