package com.cabinet.patient.dto;

import java.time.LocalDate;

import com.cabinet.patient.enums.GroupeSanguin;

public record PatientResponse(
		Long id,
		String nom,
		String prenom,
		LocalDate dateNaissance,
		String email,
		String telephone,
		String adresse,
		GroupeSanguin groupeSanguin) {
}
