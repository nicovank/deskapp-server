package edu.oswego.reslife.deskapp.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.mindrot.jbcrypt.BCrypt;

public class Employee {

	/**
	 * Position of the employee. Must be coherent with the constraint on the table.
	 */
	public enum Position {
		RHD, AHD, RA, DA
	}

	private String id;
	private String building;
	private String firstName;
	private String lastName;
	private Position position;
	private String email;
	private String password;
	private String phoneNb;

	public void setID(String id) {
		this.id = id;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Utility, sets the position from the string representation of the enum's value of the employee's position.
	 *
	 * @param position a string representing the position of the employee.
	 */
	public void setPosition(String position) {
		switch (position) {
			case "RHD":
				setPosition(Position.RHD);
				break;
			case "AHD":
				setPosition(Position.AHD);
				break;
			case "RA":
				setPosition(Position.RA);
				break;
			case "DA":
				setPosition(Position.DA);
				break;
		}
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNb(String phoneNb) {
		this.phoneNb = phoneNb;
	}

	public String getID() {
		return id;
	}

	@JsonIgnore
	public String getBuilding() {
		return building;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Position getPosition() {
		return position;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNb() {
		return phoneNb;
	}
}
