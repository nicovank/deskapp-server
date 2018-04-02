package edu.oswego.reslife.deskapp.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

public class RentedEquipmentRecord {

	public static final class Resident {
		@JsonProperty("id")
		public String id;

		@JsonProperty("firstName")
		public String firstName;

		@JsonProperty("lastName")
		public String lastName;
	}

	public static final class Employee {
		@JsonProperty("id")
		public String id;

		@JsonProperty("firstName")
		public String firstName;

		@JsonProperty("lastName")
		public String lastName;
	}

	public static final class Equipment {
		@JsonProperty("id")
		public String id;

		@JsonProperty("name")
		public String name;
	}

	@JsonProperty("id")
	public String rentID;

	@JsonProperty("timeOut")
	public Timestamp timeOut;

	@JsonProperty("timeIn")
	public Timestamp timeIn;

	@JsonProperty("condition")
	public String condition;

	@JsonProperty("resident")
	public Resident resident;

	@JsonProperty("employee")
	public Employee employee;

	@JsonProperty("equipment")
	public Equipment equipment;
}
