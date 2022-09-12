package com.crnr.hcms.assignment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@Entity
@Table(name = "assignment_address")
@NoArgsConstructor
@AllArgsConstructor
public class AddressModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long addressId;

	@Column(name = "address_type", nullable = false)
	private String addressType;
	
	@Column(name = "address_line_one", nullable = false)
	private String addressLineOne;

	@Column(name = "address_line_two")
	private String addressLineTwo;
	
	@Column(name = "city", nullable = false)
	private String city;
	
	@Column(name = "state", nullable = false)
	private String state;
	
	@Column(name = "country", nullable = false)
	private String country;
	
	@Column(name = "pin_code", nullable = false)
	private Long pinCode;

}
