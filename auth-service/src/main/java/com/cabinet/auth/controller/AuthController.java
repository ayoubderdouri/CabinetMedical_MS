package com.cabinet.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabinet.auth.dto.CredentialsRequest;
import com.cabinet.auth.dto.RegistrationRequest;
import com.cabinet.auth.dto.TokenResponse;
import com.cabinet.auth.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegistrationRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
	}

	@PostMapping("/login")
	public TokenResponse login(@Valid @RequestBody CredentialsRequest request) {
		return authService.login(request);
	}
}
