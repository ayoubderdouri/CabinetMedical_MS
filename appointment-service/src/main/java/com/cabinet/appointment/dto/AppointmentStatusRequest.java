package com.cabinet.appointment.dto;

import com.cabinet.appointment.model.AppointmentStatus;

import jakarta.validation.constraints.NotNull;

public record AppointmentStatusRequest(@NotNull AppointmentStatus status) {
}
