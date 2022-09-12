package com.crnr.hcms.assignment.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
@NoArgsConstructor
public class PatientDto implements Serializable {
	
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	private String patientId;

	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@NotNull(message = "Date of birth is mandatory")
    private LocalDateTime dateOfBirth;
	
	@NotBlank(message = "Gender is mandatory")
	// @Pattern(regexp = "MALE|FEMALE|TRANSGENDER", flags = Pattern.Flag.CASE_INSENSITIVE)
	@Pattern(regexp = "MALE|FEMALE|TRANSGENDER")
	private String gender;
	
	private String status;
	
	@NotBlank(message = "Primary contact country code is mandatory")
	private String primaryContactCountryCode;
	
	@NotNull(message = "Primary contact number is mandatory")
	private Long primaryContactNumber;
	
	private String secondaryContactCountryCode;
	
	private Long secondaryContactNumber;
	
	@NotNull(message = "Address is mandatory")
    @Valid
	private List<AddressDto> addresses;

}
