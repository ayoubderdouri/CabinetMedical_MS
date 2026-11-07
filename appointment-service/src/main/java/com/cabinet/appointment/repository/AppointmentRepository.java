package com.cabinet.appointment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinet.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findAllByOrderByDateHeureAsc();
}
