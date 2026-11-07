package com.cabinet.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cabinet.appointment.dto.AppointmentRequest;
import com.cabinet.appointment.dto.AppointmentResponse;
import com.cabinet.appointment.model.AppointmentStatus;
import com.cabinet.appointment.service.AppointmentService;

@SpringBootTest(properties = "security.jwt.secret=test-signing-key-with-at-least-32-bytes")
class AppointmentServiceApplicationTests {

	@Autowired
	private AppointmentService appointmentService;

	@Test
	void contextLoads() {
	}

	@Test
	void createsAndUpdatesAppointment() {
		AppointmentRequest request = new AppointmentRequest(
				1L, LocalDateTime.now().plusDays(1), "Consultation");

		AppointmentResponse created = appointmentService.create(request);
		AppointmentResponse updated = appointmentService.updateStatus(created.id(), AppointmentStatus.CONFIRMED);

		assertEquals(1L, updated.patientId());
		assertEquals(AppointmentStatus.CONFIRMED, updated.status());
		assertEquals(created.id(), appointmentService.findById(created.id()).id());
	}
}
