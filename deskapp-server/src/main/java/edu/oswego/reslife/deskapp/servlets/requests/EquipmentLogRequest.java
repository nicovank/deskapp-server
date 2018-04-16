package edu.oswego.reslife.deskapp.servlets.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EquipmentLogRequest {
	private String residentID;
	private String equipmentID;

	@JsonProperty("resident")
	public String getResidentID() {
		return residentID;
	}

	@JsonProperty("equipment")
	public String getEquipmentID() {
		return equipmentID;
	}
}
