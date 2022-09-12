package com.crnr.hcms.assignment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crnr.hcms.assignment.model.PatientModel;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
@Repository
public interface PatientRepository extends JpaRepository<PatientModel, String> {
	
	Optional<PatientModel> findByPatientIdAndIsDeletedFalse(String patient_id);

}
