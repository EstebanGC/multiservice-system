package com.example.cart_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
class CartServiceApplicationTests {

	@Autowired
	JwtDecoder jwtDecoder;

	@Test
	void contextLoads() {
	}


	@Test
	void testDecodeToken() {
		String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJpcXVhcmsiLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImlhdCI6MTc1MjY1MTYxNSwiZXhwIjoxNzUyNzM4MDE1fQ.Kjz4Mds6J9WWbGPI2xS7OgkEMIgM_baSmct1XMFdDAFh0tLGcHgDrbw5cE79BdP1";
		Jwt jwt = jwtDecoder.decode(token);
		System.out.println(jwt.getSubject());  // debería imprimir "iquark"
	}

}
