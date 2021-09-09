package com.wallmart.takehomeassignment.exception;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceError {

	private List<String> message;
	private int status;
	private String timeStamp;

	public ServiceError(List<String> message) {
		super();
		this.message = message;
		this.status = 400;
		this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC).toString();
	}

	public ServiceError(List<String> message, int httpStatus) {
		super();
		this.message = message;
		this.status = httpStatus;
		this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC).toString();
	}
}
