package com.cabinet.auth;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cabinet.auth.dto.CredentialsRequest;
import com.cabinet.auth.dto.RegistrationRequest;
import com.cabinet.auth.dto.TokenResponse;
import com.cabinet.auth.repository.UserAccountRepository;
import com.cabinet.auth.service.AuthService;

@SpringBootTest(properties = "security.jwt.secret=test-signing-key-with-at-least-32-bytes")
class AuthServiceApplicationTests {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void registersAndAuthenticatesWithHashedPasswordAndJwt() {
		String email = UUID.randomUUID() + "@example.com";
		String password = "StrongPassword123";

		TokenResponse registration = authService.register(new RegistrationRequest(email.toUpperCase(), password));
		TokenResponse login = authService.login(new CredentialsRequest(email, password));

		assertNotNull(registration.accessToken());
		assertTrue(registration.accessToken().startsWith("eyJ"));
		assertEquals("Bearer", login.tokenType());
		assertNotEquals(password, userAccountRepository.findByEmail(email).orElseThrow().getPasswordHash());
	}
}
