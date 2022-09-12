package com.crnr.hcms.assignment.controller;

import static com.crnr.hcms.assignment.constant.ApiConstants.API.API_PATIENTS_PATH;
import static com.crnr.hcms.assignment.constant.ApiConstants.API.API_VERSION;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crnr.hcms.assignment.dto.Filter;
import com.crnr.hcms.assignment.dto.PatientDto;
import com.crnr.hcms.assignment.dto.PatientUpdateReqDto;
import com.crnr.hcms.assignment.dto.ResponseDto;
import com.crnr.hcms.assignment.service.PatientService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * The {@code PatientApi} class represents the controller layer to accept all
 * requests related to a <b> patient </b>
 * 
 * 
 * @author Namadev
 * 
 * @version 1.0
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(API_VERSION + API_PATIENTS_PATH)
public class PatientApi {

	@Autowired
	private PatientService patientService;

	/**
	 * Create a single {@code Patient} and return the created REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientDtoRequest - the {@code Patient} to create, accepted as JSON -
	 *                          ID is not required, it will be ignored
	 * 
	 * @return responseDto - created patient details
	 */
	@PostMapping
	public ResponseEntity<ResponseDto> createPatient(@Valid @RequestBody PatientDto patientDtoRequest) {

		log.info("PatientApi --> createPatient");
		return patientService.createPatient(patientDtoRequest);
	}

	/**
	 * Look up a single {@code Patient} and transform it into a REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId - the patient identifier
	 * 
	 * @return responseDto - with single patient details
	 */
	@GetMapping("/{patient_id}")
	public ResponseEntity<ResponseDto> findById(@PathVariable("patient_id") String patientId) {

		log.info("PatientApi --> findById");
		return patientService.findById(patientId);
	}

	/**
	 * Update a single {@code Patient} and return the updated REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId           - the patient identifier
	 * @param patientUpdateReqDto - the {@code Patient} to update, accepted as JSON
	 *                            - ID is not required, it will be ignored
	 * 
	 * @return responseDto - with updated patient details
	 */
	@PutMapping(value = "/{patient_id}")
	public ResponseEntity<ResponseDto> updatePatient(@PathVariable(value = "patient_id") String patientId,
			@Valid @RequestBody PatientUpdateReqDto patientUpdateReqDto) {

		log.info("PatientApi --> updatePatient");
		return patientService.updatePatient(patientId, patientUpdateReqDto);
	}

	/**
	 * Delete a single {@code Patient} and return the http status code OK. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId - the patient identifier
	 * 
	 * @return responseDto - with status code OK
	 */
	@DeleteMapping("/{patient_id}")
	public ResponseEntity<ResponseDto> deletePatient(@PathVariable("patient_id") String patientId) {

		log.info("LandingPageAPI --> deleteLandingPage");
		return patientService.deletePatient(patientId);
	}

	/**
	 * Look up all Patients with filter and search, and transform them into a REST
	 * collection resource. Then return them through Spring Web's
	 * {@code ResponseEntity} fluent API.
	 * 
	 * @param page   - the page number
	 * @param size   - the no. of records to display in a page
	 * @param gender - the gender is filter parameter
	 * @param status - the status is filter parameter
	 * @param search - the search used to search the record by name
	 * @param order  - the order of records
	 * 
	 * @return responseDto - list of patients
	 */
	@GetMapping("/patientbyfilter")
	public ResponseEntity<ResponseDto> getPatientByFilter(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "size", required = true) int size,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "order", required = true) String order) {
		return patientService.getPatientByFilter(Filter.builder().page(page).size(size).gender(gender).status(status)
				.search(search).order(order).build());
	}

}
