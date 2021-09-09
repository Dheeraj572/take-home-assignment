package com.wallmart.takehomeassignment.model;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Data {

	private int id;
	private int temperature;
	private int humidity;
	private String location;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private String timeStamp;

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date today) {
		this.timeStamp = ZonedDateTime.now(ZoneOffset.UTC).toString();
	}

}
