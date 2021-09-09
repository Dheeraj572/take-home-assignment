package com.wallmart.takehomeassignment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallmart.takehomeassignment.exception.ServiceException;
import com.wallmart.takehomeassignment.model.TelemetryResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TelemetryService implements ITelemetryService {

	@Value("${telemetryURL}")
	private String telemetryURL;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public TelemetryResponse retrieveTelemetry(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws JsonMappingException, JsonProcessingException {

		log.debug("Making service call to telemetry ---- ");

		TelemetryResponse data = null;
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_XML);
		List<MediaType> mediaTypeList = new ArrayList<>();
		mediaTypeList.add(MediaType.APPLICATION_XML);
		httpHeader.setAccept(mediaTypeList);
		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeader);

		try {
			ResponseEntity<String> exchange = restTemplate.exchange(telemetryURL, HttpMethod.GET, httpEntity,
					String.class);
			JSONObject jsonObject = XML.toJSONObject(exchange.getBody());
			data = objectMapper.readValue(String.valueOf(jsonObject), TelemetryResponse.class);
			data.getData().setTimeStamp(new Date());
		} catch (HttpClientErrorException exception) {
			log.error("Error while retrieving telemetry ---" + exception.getMessage());
			throw new ServiceException("Error while retrieving telemetry ---" + exception.getMessage(),
					httpServletResponse.getStatus());
		} catch (Exception exception) {
			log.error("Error while retrieving telemetry ---" + exception.getMessage());
			throw new ServiceException("Error while retrieving telemetry ---" + exception.getMessage(),
					httpServletResponse.getStatus());
		}

//		Optional<TelemetryResponse> dataOptional = Optional.ofNullable(dataResponseEntity).map(entity -> entity.getBody());
//		if (dataOptional.isPresent()) {
//			data = dataOptional.get();
//		}
//		log.debug("Service call to telemetry End---- ");
		return data;
	}

}
