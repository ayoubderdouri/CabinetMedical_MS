package com.cabinet.patient.dto;

import java.time.LocalDate;

import com.cabinet.patient.enums.GroupeSanguin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record PatientRequest(
		@NotBlank String nom,
		@NotBlank String prenom,
		@NotNull @Past LocalDate dateNaissance,
		@NotBlank @Email String email,
		String telephone,
		String adresse,
		@NotNull GroupeSanguin groupeSanguin) {
}
