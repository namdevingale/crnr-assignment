package com.crnr.hcms.assignment.dto;

import java.io.Serializable;

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
public class AddressDto implements Serializable {
	
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	
	private long addressId;

	@NotBlank(message = "Address type is mandatory")
	// @Pattern(regexp = "PERMANENT|PRESENT|OFFICE", flags = Pattern.Flag.CASE_INSENSITIVE)
	@Pattern(regexp = "PERMANENT|PRESENT|OFFICE")
	private String addressType;
	
	@NotBlank(message = "Address line 1 is mandatory")
	private String addressLineOne;

	private String addressLineTwo;
	
	@NotBlank(message = "City is mandatory")
	private String city;
	
	@NotBlank(message = "State is mandatory")
	private String state;
	
	@NotBlank(message = "Country is mandatory")
	private String country;
	
	@NotNull(message = "Pin code is mandatory")
	private Long pinCode;

}
