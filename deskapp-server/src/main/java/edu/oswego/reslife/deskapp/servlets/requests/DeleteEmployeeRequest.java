package edu.oswego.reslife.deskapp.servlets.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteEmployeeRequest {
	private String id;

	@JsonProperty("id")
	public String getID() {
		return id;
	}
}
