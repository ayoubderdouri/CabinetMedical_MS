package com.cabinet.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinet.patient.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
