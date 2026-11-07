package com.cabinet.patient.mapper;

import org.springframework.stereotype.Component;

import com.cabinet.patient.dto.PatientRequest;
import com.cabinet.patient.dto.PatientResponse;
import com.cabinet.patient.model.Patient;

@Component
public class PatientMapper {

	public Patient toEntity(PatientRequest request) {
		return new Patient(
				null,
				request.nom(),
				request.prenom(),
				request.dateNaissance(),
				request.email(),
				request.telephone(),
				request.adresse(),
				request.groupeSanguin());
	}

	public void updateEntity(Patient patient, PatientRequest request) {
		patient.setNom(request.nom());
		patient.setPrenom(request.prenom());
		patient.setDateNaissance(request.dateNaissance());
		patient.setEmail(request.email());
		patient.setTelephone(request.telephone());
		patient.setAdresse(request.adresse());
		patient.setGroupeSanguin(request.groupeSanguin());
	}

	public PatientResponse toResponse(Patient patient) {
		return new PatientResponse(
				patient.getId(),
				patient.getNom(),
				patient.getPrenom(),
				patient.getDateNaissance(),
				patient.getEmail(),
				patient.getTelephone(),
				patient.getAdresse(),
				patient.getGroupeSanguin());
	}
}
