package com.wallmart.takehomeassignment.exception.handling;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.wallmart.takehomeassignment.exception.ServiceError;
import com.wallmart.takehomeassignment.exception.ServiceException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = ServiceException.class)
	public ServiceError handleServiceException(ServiceException ex) {
		List<String> errorList = new ArrayList<>();
		errorList.add(ex.getMessage());
		return new ServiceError(errorList, ex.getHttpStatus());
	}

}
