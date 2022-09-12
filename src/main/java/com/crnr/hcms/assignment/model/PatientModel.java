package com.crnr.hcms.assignment.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "assignment_patient")
@NoArgsConstructor
public class PatientModel extends AuditModel {
	
	@Id
	@GenericGenerator(name = "assigned-sequence", strategy = "com.crnr.hcms.assignment.model.helper.ModelSequenceGenerator", parameters = {
			@org.hibernate.annotations.Parameter(name = "sequence_name", value = "assignment_patient_id_seq"),
			@org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "PT_") })
	@GeneratedValue(generator = "assigned-sequence", strategy = GenerationType.SEQUENCE)
	@Column(name = "patient_id")
	private String patientId;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "date_of_birth", nullable = false)
    private LocalDateTime dateOfBirth;
	
	@Column(name = "gender", nullable = false)
	private String gender;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "primary_contact_country_code", nullable = false)
	private String primaryContactCountryCode;
	
	@Column(name = "primary_contact_number", nullable = false)
	private Long primaryContactNumber;
	
	@Column(name = "secondary_contact_country_code")
	private String secondaryContactCountryCode;
	
	@Column(name = "secondary_contact_number")
	private Long secondaryContactNumber;
	
	@OneToMany(targetEntity = AddressModel.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "patient_id", referencedColumnName = "patient_id",  nullable = false)
	private List<AddressModel> addresses;

}
