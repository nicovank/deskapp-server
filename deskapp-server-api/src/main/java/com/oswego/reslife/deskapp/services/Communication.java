package com.oswego.reslife.deskapp.services;

import com.oswego.reslife.deskapp.sql.SQLQueryManager;
import com.oswego.reslife.deskapp.sql.SQLService;
import com.oswego.reslife.deskapp.sql.models.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service("communication")
@Path("communication")
public class Communication {

	/**
	 * Handles POST requests to /communication/list.
	 * Reads the latest messages for a particular building.
	 *
	 * @param body     The body of the request, that will be parsed using Jackson.
	 */
	@Path("list")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public ArrayList<Message> listMessages(Request req) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLService.getSQLConnection();
			SQLQueryManager manager = SQLService.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("communication.get.messages"));
			statement.setString(1, "MACKIN");
			statement.setInt(2, 0);
			statement.setInt(3, 10);
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
			e.printStackTrace();
		} finally {
			// Close all connections
			try {
				if (results != null) results.close();
				if (statement != null) statement.close();
				if (connection != null) connection.close();
			} catch (SQLException ignored) {

			}
		}

		return null;
	}
}
