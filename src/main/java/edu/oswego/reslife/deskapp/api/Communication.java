package edu.oswego.reslife.deskapp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.oswego.reslife.deskapp.errors.InternalError;
import edu.oswego.reslife.deskapp.sql.SQLConnection;
import edu.oswego.reslife.deskapp.sql.SQLQueryManager;
import edu.oswego.reslife.deskapp.sql.models.Message;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/communication")
public class Communication {

	/**
	 * The request class that will be used by the listMessages method.
	 */
	private static final class ListRequest {
		@JsonProperty("page")
		int page;
	}

	private static final int MESSAGES_PER_PAGE = 10;

	/**
	 * Handles POST requests to /communication/list.
	 * Reads the latest messages for a particular building.
	 *
	 * @param req The request sent by the client
	 * @return An list of the latest messages
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ArrayList<Message> listMessages(@RequestBody ListRequest req) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("communication.get.messages"));
			statement.setString(1, "MACKIN");
			statement.setInt(2, req.page * MESSAGES_PER_PAGE);
			statement.setInt(3, (req.page + 1) * MESSAGES_PER_PAGE);
			results = statement.executeQuery();

			ArrayList<Message> messages = new ArrayList<>();

			while (results.next()) {
				Message message = new Message();

				message.setId(results.getInt("ID"));
				message.setFirstName(results.getString("First_Name"));
				message.setLastName(results.getString("Last_Name"));
				message.setTime(results.getTimestamp("Time"));
				message.setMessage(results.getString("Message"));

				messages.add(message);
			}

			return messages;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new InternalError(e.getMessage());
		} finally {
			// Close all connections
			try {
				if (results != null) results.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException ignored) {

			}
		}
	}
}
