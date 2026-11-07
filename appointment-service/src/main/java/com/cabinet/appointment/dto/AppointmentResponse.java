package com.cabinet.appointment.dto;

import java.time.LocalDateTime;

import com.cabinet.appointment.model.AppointmentStatus;

public record AppointmentResponse(
		Long id,
		Long patientId,
		LocalDateTime dateHeure,
		String motif,
		AppointmentStatus status) {
}
