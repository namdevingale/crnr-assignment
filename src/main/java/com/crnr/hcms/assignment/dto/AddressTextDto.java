package com.crnr.hcms.assignment.dto;

import org.eclipse.swt.widgets.Text;

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
public class AddressTextDto {
	
	private Text addressId;

	private Text addressType;
	
	private Text addressLineOne;

	private Text addressLineTwo;

	private Text city;
	
	private Text state;
	
	private Text country;
	
	private Text pinCode;

}
