package com.cabinet.patient.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinet.patient.dto.PatientRequest;
import com.cabinet.patient.dto.PatientResponse;
import com.cabinet.patient.exception.ResourceNotFoundException;
import com.cabinet.patient.mapper.PatientMapper;
import com.cabinet.patient.model.Patient;
import com.cabinet.patient.repository.PatientRepository;
import com.cabinet.patient.service.PatientService;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;
	private final PatientMapper patientMapper;

	public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
		this.patientRepository = patientRepository;
		this.patientMapper = patientMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<PatientResponse> findAll() {
		return patientRepository.findAll().stream()
				.map(patientMapper::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PatientResponse findById(Long id) {
		return patientMapper.toResponse(getPatient(id));
	}

	@Override
	public PatientResponse create(PatientRequest request) {
		Patient patient = patientRepository.save(patientMapper.toEntity(request));
		return patientMapper.toResponse(patient);
	}

	@Override
	public PatientResponse update(Long id, PatientRequest request) {
		Patient patient = getPatient(id);
		patientMapper.updateEntity(patient, request);
		return patientMapper.toResponse(patientRepository.save(patient));
	}

	@Override
	public void delete(Long id) {
		patientRepository.delete(getPatient(id));
	}

	private Patient getPatient(Long id) {
		return patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient introuvable avec l'identifiant " + id));
	}
}
