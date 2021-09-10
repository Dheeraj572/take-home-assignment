package com.wallmart.takehomeassignment.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wallmart.takehomeassignment.exception.ServiceError;
import com.wallmart.takehomeassignment.service.ITelemetryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("telemetry")
public class TelemetryController {

	@Autowired
	private ITelemetryService iTelemetryService;

	@ResponseStatus(code = HttpStatus.OK)
	@ApiOperation(value = "Retrive Telemetry Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Boolean.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = ServiceError.class) })
	@GetMapping
	public ResponseEntity<?> retrieveTelemetry(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {

		log.debug("Telemetry controller start ---");
		iTelemetryService.writeTelemetryToFile(httpServletRequest, httpServletResponse);
		log.debug("Telemetry controller end ---");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

}
