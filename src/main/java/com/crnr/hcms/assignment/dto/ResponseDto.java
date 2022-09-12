package com.crnr.hcms.assignment.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Namadev
 * 
 * @version 1.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseDto {

	private String action;
	private String result;
	private String message;
	private Object data;
	private HttpStatus status;
	private Integer code;
	private Long totalCount;
	private Integer currentPage;

}
