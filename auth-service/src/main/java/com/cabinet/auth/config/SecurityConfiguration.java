package com.cabinet.auth.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cabinet.auth.service.AuthService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class SecurityConfiguration {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								"/api/auth/register",
								"/api/auth/login",
								"/actuator/health/**",
								"/actuator/info").permitAll()
						.anyRequest().denyAll())
				.httpBasic(basic -> basic.disable())
				.formLogin(form -> form.disable())
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtEncoder jwtEncoder(@Value("${security.jwt.secret}") String secret) {
		SecretKey secretKey = AuthService.secretKey(secret);
		return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
	}
}
