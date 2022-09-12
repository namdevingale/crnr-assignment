package com.crnr.hcms.assignment.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@Builder
public class Filter implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private int page;
	private int size;
	private String gender;
	private String status;
	private String order;
	private String search;

}
