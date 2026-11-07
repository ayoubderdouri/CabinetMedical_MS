package com.cabinet.auth.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cabinet.auth.dto.CredentialsRequest;
import com.cabinet.auth.dto.RegistrationRequest;
import com.cabinet.auth.dto.TokenResponse;
import com.cabinet.auth.model.UserAccount;
import com.cabinet.auth.repository.UserAccountRepository;

@Service
public class AuthService {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtEncoder;
	private final String issuer;
	private final Duration expiration;

	public AuthService(
			UserAccountRepository userAccountRepository,
			PasswordEncoder passwordEncoder,
			JwtEncoder jwtEncoder,
			@Value("${security.jwt.issuer}") String issuer,
			@Value("${security.jwt.expiration}") Duration expiration) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtEncoder = jwtEncoder;
		this.issuer = issuer;
		this.expiration = expiration;
	}

	@Transactional
	public TokenResponse register(RegistrationRequest request) {
		String email = normalizeEmail(request.email());
		if (userAccountRepository.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
		}
		userAccountRepository.save(new UserAccount(email, passwordEncoder.encode(request.password())));
		return createToken(email);
	}

	@Transactional(readOnly = true)
	public TokenResponse login(CredentialsRequest request) {
		String email = normalizeEmail(request.email());
		UserAccount account = userAccountRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
		if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
		}
		return createToken(email);
	}

	private TokenResponse createToken(String email) {
		Instant issuedAt = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(issuer)
				.issuedAt(issuedAt)
				.expiresAt(issuedAt.plus(expiration))
				.subject(email)
				.claim("scope", "USER")
				.build();
		String token = jwtEncoder.encode(JwtEncoderParameters.from(
				JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
		return new TokenResponse(token, "Bearer", expiration.toSeconds());
	}

	private String normalizeEmail(String email) {
		return email.trim().toLowerCase(Locale.ROOT);
	}

	public static SecretKey secretKey(String secret) {
		byte[] key = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
		if (key.length < 32) {
			throw new IllegalArgumentException("JWT_SECRET must be at least 32 UTF-8 bytes for HS256");
		}
		return new SecretKeySpec(key, "HmacSHA256");
	}
}
