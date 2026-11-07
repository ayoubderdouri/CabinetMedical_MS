package com.cabinet.appointment.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AppointmentRequest(
		@NotNull @Positive Long patientId,
		@NotNull @Future LocalDateTime dateHeure,
		@NotBlank @Size(max = 500) String motif) {
}
