package com.crnr.hcms.assignment.service;

import org.springframework.http.ResponseEntity;

import com.crnr.hcms.assignment.dto.Filter;
import com.crnr.hcms.assignment.dto.PatientDto;
import com.crnr.hcms.assignment.dto.PatientUpdateReqDto;
import com.crnr.hcms.assignment.dto.ResponseDto;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
public interface PatientService {

	public ResponseEntity<ResponseDto> createPatient(PatientDto patientDtoRequest);

	public ResponseEntity<ResponseDto> findById(String patientId);

	public ResponseEntity<ResponseDto> updatePatient(String patientId, PatientUpdateReqDto patientUpdateReqDto);
	
	public ResponseEntity<ResponseDto> deletePatient(String patientId);

	public ResponseEntity<ResponseDto> getPatientByFilter(Filter filter);

}
