package com.oswego.reslife.deskapp.utils;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * A utility class, that can notably be used to return a error message to the client.
 */
public class Error {

	/**
	 * Message class so that Jackson wraps the JSON.
	 */
	private static final class Message {
		@JsonProperty("message")
		String message;
	}

	private Message message;

	/**
	 * Creates a new error, storing the reason as the message.
	 * The reason should indicate what failed.
	 *
	 * @param reason the reason the error occurred.
	 */
	public Error(String reason) {
		message = new Message();
		message.message = reason;
	}

	/**
	 * Returns the message corresponding to the error that occurred.
	 *
	 * @ the message corresponding to the error that occurred.
	 */
	@JsonProperty("error")
	public Message getMessage() {
		return message;
	}
}
