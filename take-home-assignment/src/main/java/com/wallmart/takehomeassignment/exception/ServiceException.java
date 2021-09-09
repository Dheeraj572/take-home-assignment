package com.wallmart.takehomeassignment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	private int httpStatus;
}
