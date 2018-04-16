package edu.oswego.reslife.deskapp.servlets.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessLogRequest {
	private String residentID;
	private String accessID;

	@JsonProperty("resident")
	public String getResidentID() {
		return residentID;
	}

	@JsonProperty("access")
	public String getAccessID() {
		return accessID;
	}
}
