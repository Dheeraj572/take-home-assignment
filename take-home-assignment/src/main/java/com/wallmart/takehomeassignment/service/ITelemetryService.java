package com.wallmart.takehomeassignment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wallmart.takehomeassignment.model.TelemetryResponse;

public interface ITelemetryService {

	TelemetryResponse retrieveTelemetry(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws JsonMappingException, JsonProcessingException;
}
