
const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

export async function http<T>(
  url: string,
  options?: RequestInit
): Promise<T> {
  const res = await fetch(`${BASE_URL}${url}`, {
    headers: {
      "Content-Type": "application/json",
      ...options?.headers,
    },
    ...options,
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || "HTTP error");
  }

  return res.json();
}