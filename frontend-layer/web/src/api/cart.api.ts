import { http } from "./http.ts";
import { getSessionId } from "../utils/session.ts";

export interface AddToCartRequest {
  productId: number;
  quantity: number;
}

export interface CartItem {
  productId: number;
  productName: string;
  price: number;
  quantity: number;
}

export interface CartResponse {
  cartId: number;
  username?: string | null;
  sessionId?: string | null;
  items: CartItem[];
}

export function addToCartGuest(payload: AddToCartRequest): Promise<CartResponse> {
  const sessionId = getSessionId();

  return http(`/cart/session/${sessionId}`, {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function getGuestCart(): Promise<CartResponse> {
  const sessionId = getSessionId();
  return http(`/cart/session/${sessionId}`);
}