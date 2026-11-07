package com.cabinet.appointment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinet.appointment.dto.AppointmentRequest;
import com.cabinet.appointment.dto.AppointmentResponse;
import com.cabinet.appointment.exception.ResourceNotFoundException;
import com.cabinet.appointment.model.Appointment;
import com.cabinet.appointment.model.AppointmentStatus;
import com.cabinet.appointment.repository.AppointmentRepository;

@Service
@Transactional
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;

	public AppointmentService(AppointmentRepository appointmentRepository) {
		this.appointmentRepository = appointmentRepository;
	}

	@Transactional(readOnly = true)
	public List<AppointmentResponse> findAll() {
		return appointmentRepository.findAllByOrderByDateHeureAsc().stream()
				.map(this::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public AppointmentResponse findById(Long id) {
		return toResponse(getAppointment(id));
	}

	public AppointmentResponse create(AppointmentRequest request) {
		Appointment appointment = new Appointment(request.patientId(), request.dateHeure(), request.motif());
		return toResponse(appointmentRepository.save(appointment));
	}

	public AppointmentResponse update(Long id, AppointmentRequest request) {
		Appointment appointment = getAppointment(id);
		appointment.setPatientId(request.patientId());
		appointment.setDateHeure(request.dateHeure());
		appointment.setMotif(request.motif());
		return toResponse(appointmentRepository.save(appointment));
	}

	public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
		Appointment appointment = getAppointment(id);
		appointment.setStatus(status);
		return toResponse(appointmentRepository.save(appointment));
	}

	public void delete(Long id) {
		appointmentRepository.delete(getAppointment(id));
	}

	private Appointment getAppointment(Long id) {
		return appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Appointment not found with id " + id));
	}

	private AppointmentResponse toResponse(Appointment appointment) {
		return new AppointmentResponse(
				appointment.getId(),
				appointment.getPatientId(),
				appointment.getDateHeure(),
				appointment.getMotif(),
				appointment.getStatus());
	}
}
