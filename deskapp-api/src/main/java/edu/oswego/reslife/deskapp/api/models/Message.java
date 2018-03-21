package edu.oswego.reslife.deskapp.api.models;

import java.sql.Timestamp;

/**
 * Models a message that could be returned via a SQL query.
 */
public class Message {
	private int id;
	private String firstName;
	private String lastName;
	private Timestamp time;
	private String message;

	/**
	 * Returns the id of that message.
	 *
	 * @return the id of that message.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the first name of the employee that posted the message.
	 *
	 * @return the first name of the employee that posted the message.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the last name of the employee that posted the message.
	 *
	 * @return the last name of the employee that posted the message.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns the time the message was posted.
	 *
	 * @return the time the message was posted.
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * Returns the actual message posted by the user.
	 *
	 * @return the message that was posted.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the id of that message.
	 *
	 * @param id the new id to be stored.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the first name of the employee that posted the message.
	 *
	 * @param firstName The first name to store.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Sets the last name of the employee that posted the message.
	 *
	 * @param lastName The last name to store.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Sets the time this message was posted.
	 *
	 * @param time the time the message was posted.
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}

	/**
	 * Stores the message that was posted by the employee.
	 *
	 * @param message the message to store.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}