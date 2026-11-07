package com.cabinet.appointment.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cabinet.appointment.dto.AppointmentRequest;
import com.cabinet.appointment.dto.AppointmentResponse;
import com.cabinet.appointment.dto.AppointmentStatusRequest;
import com.cabinet.appointment.service.AppointmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

	private final AppointmentService appointmentService;

	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	@GetMapping
	public List<AppointmentResponse> findAll() {
		return appointmentService.findAll();
	}

	@GetMapping("/{id}")
	public AppointmentResponse findById(@PathVariable Long id) {
		return appointmentService.findById(id);
	}

	@PostMapping
	public ResponseEntity<AppointmentResponse> create(@Valid @RequestBody AppointmentRequest request) {
		AppointmentResponse response = appointmentService.create(request);
		return ResponseEntity.created(URI.create("/api/appointments/" + response.id())).body(response);
	}

	@PutMapping("/{id}")
	public AppointmentResponse update(
			@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) {
		return appointmentService.update(id, request);
	}

	@PatchMapping("/{id}/status")
	public AppointmentResponse updateStatus(
			@PathVariable Long id, @Valid @RequestBody AppointmentStatusRequest request) {
		return appointmentService.updateStatus(id, request.status());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		appointmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
