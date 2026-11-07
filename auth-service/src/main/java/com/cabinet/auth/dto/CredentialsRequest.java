package com.cabinet.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CredentialsRequest(
		@NotBlank @Email String email,
		@NotBlank String password) {
}
