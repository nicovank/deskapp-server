package edu.oswego.reslife.deskapp.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class TransactionException extends Exception {

	private static final class JSONMessage {
		@JsonProperty("error")
		private String message;

		public JSONMessage(String message) {
			this.message = message;
		}
	}

	private String message;

	/**
	 * Creates an Exception with a default message.
	 */
	public TransactionException() {
		this("An error has occurred. Please try again.");
	}

	/**
	 * Creates a new Exception with the given message.
	 *
	 * @param message a message explaining why the exception was thrown.
	 */
	public TransactionException(String message) {
		this.message = message;
	}

	/**
	 * Creates a new Exception from given exception.
	 *
	 * @param e The exception that caused a problem.
	 */
	public TransactionException(Exception e) {
		this.message = e.getLocalizedMessage();
	}

	/**
	 * Writes the message to the given output stream.
	 */
	public void writeMessageAsJson(OutputStream out) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, new JSONMessage(message));
	}

	/**
	 * Writes the message to a writer.
	 */
	public void writeMessageAsJson(Writer out) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, new JSONMessage(message));
	}
}
