package com.wallmart.takehomeassignment.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ITelemetryService {

	void writeTelemetryToFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException,JsonMappingException, JsonProcessingException;
}
