package com.wallmart.takehomeassignment.service;

import java.io.FileWriter;
import java.io.IOException;
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
import org.springframework.scheduling.annotation.Scheduled;
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
	public void writeTelemetryToFile(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		writeTelemetryJob();
	}

	@Scheduled(cron = "0 0/5 * * * *")
	private void writeTelemetryJob() throws IOException {

		log.info("Making service call to telemetry ---- ");
		TelemetryResponse telemetryResponse = retrieveTelemetry();
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(
					"logs/telemetryResponse" + String.valueOf(telemetryResponse.getData().getId()) + ".txt");
			fileWriter.write(String.valueOf(telemetryResponse));

		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), 500);
		} finally {
			fileWriter.flush();
			fileWriter.close();
		}
	}

	@Scheduled(cron = "0 * * * * *")
	private TelemetryResponse retrieveTelemetry() {

		System.out.println("Retrieve Telemetry");
		TelemetryResponse telemetryResponse = null;

		try {

			HttpEntity<?> httpEntity = prepareHttpEntityForTelemetry();

			ResponseEntity<String> telemetryResponseXML = restTemplate.exchange(telemetryURL, HttpMethod.GET,
					httpEntity, String.class);

			if (telemetryResponseXML != null && telemetryResponseXML.getBody() != null) {

				telemetryResponse = convertXMLTelemetryToJSON(telemetryResponseXML, telemetryResponse);
				logTelemetryResponse(telemetryResponse);

			} else {
				log.error("Error while retrieving telemetry ---" + "Telemetry response is null");
				throw new ServiceException("Error while retrieving telemetry ---" + "Telemetry response is null", 500);
			}

		} catch (HttpClientErrorException exception) {
			log.error("Error while retrieving telemetry ---" + exception.getMessage());
			throw new ServiceException("Error while retrieving telemetry ---" + exception.getMessage(), 500);
		} catch (Exception exception) {
			log.error("Error while retrieving telemetry ---" + exception.getMessage());
			throw new ServiceException("Error while retrieving telemetry ---" + exception.getMessage(), 500);
		}

		return telemetryResponse;
	}

	private HttpEntity<?> prepareHttpEntityForTelemetry() {

		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_XML);
		List<MediaType> mediaTypeList = new ArrayList<>();
		mediaTypeList.add(MediaType.APPLICATION_XML);
		httpHeader.setAccept(mediaTypeList);
		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeader);

		return httpEntity;
	}

	private TelemetryResponse convertXMLTelemetryToJSON(ResponseEntity<String> telemetryResponseXML,
			TelemetryResponse telemetryResponse) throws JsonMappingException, JsonProcessingException {

		JSONObject jsonObject = XML.toJSONObject(telemetryResponseXML.getBody());
		telemetryResponse = objectMapper.readValue(String.valueOf(jsonObject), TelemetryResponse.class);

		if (telemetryResponse != null && telemetryResponse.getData() != null) {
			telemetryResponse.getData().setTimeStamp(new Date());
		}

		return telemetryResponse;
	}

	private void logTelemetryResponse(TelemetryResponse telemetryResponse) {
		log.info("Telemetry Response ---" + String.valueOf(telemetryResponse));
	}

}
