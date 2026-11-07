package com.cabinet.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cabinet.patient.dto.PatientRequest;
import com.cabinet.patient.dto.PatientResponse;
import com.cabinet.patient.enums.GroupeSanguin;
import com.cabinet.patient.exception.ResourceNotFoundException;
import com.cabinet.patient.service.PatientService;

@SpringBootTest
class PatientServiceApplicationTests {

	@Autowired
	private PatientService patientService;

	@Test
	void contextLoads() {
	}

	@Test
	void createsUpdatesAndDeletesPatient() {
		PatientRequest request = new PatientRequest(
				"Alami",
				"Sara",
				LocalDate.now().minusYears(30),
				UUID.randomUUID() + "@example.com",
				"0600000000",
				"Casablanca",
				GroupeSanguin.O_POSITIF);

		PatientResponse created = patientService.create(request);
		PatientResponse updated = patientService.update(created.id(), new PatientRequest(
				"Alami",
				"Sara",
				request.dateNaissance(),
				request.email(),
				request.telephone(),
				"Rabat",
				request.groupeSanguin()));

		assertEquals("Rabat", updated.adresse());
		assertEquals(created.id(), patientService.findById(created.id()).id());
		patientService.delete(created.id());
		assertThrows(ResourceNotFoundException.class, () -> patientService.findById(created.id()));
	}
}
