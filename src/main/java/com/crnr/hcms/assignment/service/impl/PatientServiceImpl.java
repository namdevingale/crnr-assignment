package com.crnr.hcms.assignment.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.crnr.hcms.assignment.dto.AddressDto;
import com.crnr.hcms.assignment.dto.Filter;
import com.crnr.hcms.assignment.dto.PatientDto;
import com.crnr.hcms.assignment.dto.PatientUpdateReqDto;
import com.crnr.hcms.assignment.dto.ResponseDto;
import com.crnr.hcms.assignment.enums.CommonFileds;
import com.crnr.hcms.assignment.model.AddressModel;
import com.crnr.hcms.assignment.model.PatientModel;
import com.crnr.hcms.assignment.query.ConditionalServicePredicate;
import com.crnr.hcms.assignment.query.CountQueryCriteria;
import com.crnr.hcms.assignment.repository.PatientRepository;
import com.crnr.hcms.assignment.service.PatientService;
import com.crnr.hcms.assignment.util.BeanUtilPropertyCheck;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * The {@code PatientServiceImpl} class represents the service layer to execute
 * business logic related to a <b> patient </b>
 * 
 * 
 * @author Namadev
 * 
 * @version 1.0
 */
@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private EntityManager entityManager;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	/**
	 * Create a single {@code Patient} and return the created REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientDtoRequest - the {@code Patient} to create, accept patient
	 *                          object
	 * 
	 * @return responseDto - created patient details
	 */
	@Override
	public ResponseEntity<ResponseDto> createPatient(PatientDto patientDtoRequest) {
		log.info("PatientServiceImpl --> createPatient");
		ResponseDto responseDto = null;
		try {
			// convert DTO to entity
			PatientModel patientModelRequest = modelMapper.map(patientDtoRequest, PatientModel.class);
			patientModelRequest.setDateOfBirth(LocalDateTime.parse(patientDtoRequest.getDateOfBirth(), formatter));
			patientModelRequest.setStatus("ACTIVE");
			List<AddressModel> addresses = new ArrayList<>();
			addresses.addAll(getAddressModels(patientDtoRequest.getAddresses()));
			patientModelRequest.setAddresses(addresses);
			PatientModel createdPatientModel = patientRepository.save(patientModelRequest);

			// convert entity to DTO
			PatientDto patientDtoResponse = modelMapper.map(createdPatientModel, PatientDto.class);
			responseDto = ResponseDto.builder().action("createPatient").result(CommonFileds.SUCCESS.getFetchType())
					.message("Successfully created the Patient").data(patientDtoResponse).code(201)
					.status(HttpStatus.CREATED).build();
		} catch (Exception e) {
			log.error("Error in createPatient: ", e);
			responseDto = ResponseDto.builder().data("").action("createPatient").result(CommonFileds.FAILURE.name())
					.message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	/**
	 * Look up a single {@code Patient} and transform it into a REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId - the patient identifier
	 * 
	 * @return responseDto - with single patient details
	 */
	@Override
	public ResponseEntity<ResponseDto> findById(String patientId) {
		log.info("PatientServiceImpl --> findById");
		ResponseDto responseDto = null;
		try {
			Optional<PatientModel> patientModel = patientRepository.findByPatientIdAndIsDeletedFalse(patientId);
			if (patientModel.isPresent()) {
				// convert entity to DTO
				PatientDto patientDtoResponse = modelMapper.map(patientModel.get(), PatientDto.class);

				responseDto = ResponseDto.builder().action("findById").result(CommonFileds.SUCCESS.getFetchType())
						.message("Successfully fetched the Patient").data(patientDtoResponse).code(200)
						.status(HttpStatus.OK).build();
			} else {
				responseDto = ResponseDto.builder().action("findById").result(CommonFileds.FAILURE.getFetchType())
						.message("Record not found").data("").code(404).status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			log.error("Error in findById: ", e);
			responseDto = ResponseDto.builder().data("").action("findById").result(CommonFileds.FAILURE.name())
					.message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	/**
	 * Update a single {@code Patient} and return the updated REST resource. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId           - the patient identifier
	 * @param patientUpdateReqDto - the {@code Patient} to update, accept patient
	 *                            object
	 * 
	 * @return responseDto - with updated patient details
	 */
	@Override
	public ResponseEntity<ResponseDto> updatePatient(String patientId, PatientUpdateReqDto patientUpdateReqDto) {
		log.info("PatientServiceImpl --> updatePatient");
		ResponseDto responseDto = null;
		try {
			Optional<PatientModel> existingPatientModel = patientRepository.findByPatientIdAndIsDeletedFalse(patientId);
			if (existingPatientModel.isPresent()) {
				BeanUtils.copyProperties(patientUpdateReqDto, existingPatientModel.get(),
						BeanUtilPropertyCheck.getNullPropertyNames(patientUpdateReqDto));
				existingPatientModel.get()
						.setDateOfBirth(LocalDateTime.parse(patientUpdateReqDto.getDateOfBirth(), formatter));
				existingPatientModel.get().getAddresses().clear();
				existingPatientModel.get().getAddresses()
						.addAll(getAddressModels(patientUpdateReqDto.getAddressList()));
				PatientModel updatedPatientModel = patientRepository.save(existingPatientModel.get());
				// convert entity to DTO
				PatientDto patientDtoResponse = modelMapper.map(updatedPatientModel, PatientDto.class);
				responseDto = ResponseDto.builder().action("updatePatient").result(CommonFileds.SUCCESS.getFetchType())
						.message("Successfully updated the Patient").data(patientDtoResponse).code(200)
						.status(HttpStatus.OK).build();
			} else {
				responseDto = ResponseDto.builder().action("updatePatient").result(CommonFileds.FAILURE.getFetchType())
						.message("Record not found").data("").code(404).status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			log.error("Error in updatePatient: ", e);
			responseDto = ResponseDto.builder().data("").action("updatePatient").result(CommonFileds.FAILURE.name())
					.message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	/**
	 * Delete a single {@code Patient} and return the http status code OK. Then
	 * return it through Spring Web's {@code ResponseEntity} fluent API.
	 * 
	 * @param patientId - the patient identifier
	 * 
	 * @return responseDto - with status code OK
	 */
	@Override
	public ResponseEntity<ResponseDto> deletePatient(String patientId) {
		log.info("PatientServiceImpl --> deletePatient");
		ResponseDto responseDto = null;
		try {
			Optional<PatientModel> existingPatientModel = patientRepository.findByPatientIdAndIsDeletedFalse(patientId);
			if (existingPatientModel.isPresent()) {
				PatientModel setPatientModel = existingPatientModel.get();
				setPatientModel.setIsDeleted(true);
				patientRepository.save(setPatientModel);
				responseDto = ResponseDto.builder().action("deletePatient").result(CommonFileds.SUCCESS.getFetchType())
						.message("Successfully deleted the Patient").data("").code(200).status(HttpStatus.OK).build();
			} else {
				responseDto = ResponseDto.builder().action("deletePatient").result(CommonFileds.FAILURE.getFetchType())
						.message("Record not found").data("").code(404).status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			log.error("Error in deletePatient: ", e);
			responseDto = ResponseDto.builder().data("").action("deletePatient").result(CommonFileds.FAILURE.name())
					.message(e.getMessage()).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	/**
	 * Look up all Patients with filter and search, and transform them into a REST
	 * collection resource. Then return them through Spring Web's
	 * {@code ResponseEntity} fluent API.
	 * 
	 * @param filterContext - accept filter object
	 * 
	 * @return responseDto - list of patients
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<ResponseDto> getPatientByFilter(Filter filterContext) {

		log.info("PatientServiceImpl --> getPatientByFilter method started");
		ResponseDto responseDto = null;
		List<PatientModel> patientModelLst = new ArrayList<>();
		try {
			Map<String, Object> patientModelMap = new HashMap<>();
			patientModelMap = findByCriteria(filterContext, filterContext.getPage(), filterContext.getSize());
			patientModelLst = (List<PatientModel>) patientModelMap.get("value");
			responseDto = ResponseDto.builder().data(mapToDto(patientModelLst)).action("Success")
					.currentPage(filterContext.getPage()).totalCount((Long) patientModelMap.get("count"))
					.message("Patients data get Successfully").status(HttpStatus.OK).build();
		} catch (Exception e) {
			log.error("Error in getPatientByFilter: ", e);
			responseDto = ResponseDto.builder().action("getPatientByFilter").result(CommonFileds.ERROR.getFetchType())
					.message(e.getMessage()).data("").status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	/**
	 * Fetching the records from database with given filter criteria along with
	 * pagination.
	 * 
	 * @param filterContext
	 * @param page
	 * @param totalRows
	 * 
	 * @return patientsDataMap - a map object, contains list of patients
	 */
	private Map<String, Object> findByCriteria(Filter filterContext, int page, int totalRows) {
		Map<String, Object> patientsDataMap = new HashMap<>();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<PatientModel> criteriaQuery = criteriaBuilder.createQuery(PatientModel.class);
		Root<PatientModel> patientModel = criteriaQuery.from(PatientModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates = ConditionalServicePredicate.getFilterQueryForPatient(filterContext, patientModel, predicates,
				criteriaBuilder);
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));

		criteriaQuery.orderBy(CountQueryCriteria.getOrderList(filterContext.getOrder(), "createdDate", criteriaBuilder,
				patientModel));
		TypedQuery<PatientModel> query = entityManager.createQuery(criteriaQuery);
		query.setFirstResult(page * totalRows);
		query.setMaxResults(totalRows);

		Long value = CountQueryCriteria.createCountQuery(criteriaBuilder, criteriaQuery, patientModel, entityManager);
		patientsDataMap.put("value", query.getResultList());
		patientsDataMap.put("count", value);
		return patientsDataMap;
	}

	/**
	 * Transforming patient models to dto's.
	 * 
	 * @param patientModels - list of patient models
	 * 
	 * @return patientDtoReponseLst - list of patient dto's
	 */
	private Object mapToDto(List<PatientModel> patientModels) {
		List<PatientDto> patientDtoReponseLst = new ArrayList<>();
		patientModels.forEach(data -> {
			// convert entity to DTO
			PatientDto patientDtoResponse = modelMapper.map(data, PatientDto.class);
			patientDtoReponseLst.add(patientDtoResponse);
		});
		return patientDtoReponseLst;
	}

	/**
	 * Transforming address dto's to address models.
	 * 
	 * @param addressDtoLst - list of address dto's
	 * 
	 * @return addressModelLst - collection of address models
	 */
	private Collection<? extends AddressModel> getAddressModels(List<AddressDto> addressDtoLst) {
		List<AddressModel> addressModelLst = new ArrayList<>();
		addressDtoLst.forEach(data -> {
			if (data.getAddressId() <= 0)
				addressModelLst.add(AddressModel.builder().addressType(data.getAddressType())
						.addressLineOne(data.getAddressLineOne()).addressLineTwo(data.getAddressLineTwo())
						.city(data.getCity()).state(data.getState()).country(data.getCountry())
						.pinCode(data.getPinCode()).build());
			else if (data.getAddressId() > 0)
				addressModelLst.add(AddressModel.builder().addressId(data.getAddressId())
						.addressType(data.getAddressType()).addressLineOne(data.getAddressLineOne())
						.addressLineTwo(data.getAddressLineTwo()).city(data.getCity()).state(data.getState())
						.country(data.getCountry()).pinCode(data.getPinCode()).build());
		});
		return addressModelLst;
	}

}
