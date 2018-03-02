package edu.oswego.reslife.deskapp.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Interface to facilitate sending an internal server error to the client.
 * Could for example be used when there is a problem communicating with the database.
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was an error communicating with the database.")
public class InternalError extends RuntimeException {

	/**
	 * Creates a new InternalError with the given message.
	 *
	 * @param message The reason the internal error occurred.
	 */
	public InternalError(String message) {
		super(message);
	}
}
