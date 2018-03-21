package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.sql.SQLConnection;
import edu.oswego.reslife.deskapp.api.sql.SQLQueryManager;
import edu.oswego.reslife.deskapp.api.models.Message;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Communication {

	private static final int DEFAULT_MESSAGES_PER_PAGE = 10;

	/**
	 * Finds the latest messages posted for a given communication log.
	 * The page parameter is used to specify which page should be loaded,
	 * with a default number of messages per page of 10.
	 *
	 * @param page       The page number to load.
	 * @param buildingID The ID of the building to load messages from.
	 * @return An array of the latest messages for the given page and building.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static Message[] listMessages(int page, String buildingID)
			throws SQLException, IOException, ClassNotFoundException {

		return listMessages(page, buildingID, DEFAULT_MESSAGES_PER_PAGE);
	}

	/**
	 * Finds the latest messages posted for a given communication log.
	 * The page parameter is used to specify which page should be loaded,
	 * specifying the number of messages per page.
	 *
	 * @param page            The page number to load.
	 * @param buildingID      The ID of the building to load messages from.
	 * @param messagesPerPage the number of messages per page in the application
	 * @return An array of the latest messages for the given page and building.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static Message[] listMessages(int page, String buildingID, int messagesPerPage)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("communication.list"));
			statement.setString(1, buildingID);
			statement.setInt(2, page * messagesPerPage);
			statement.setInt(3, (page + 1) * messagesPerPage);
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

			Message[] ret = new Message[messages.size()];
			messages.toArray(ret);
			return ret;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}

	/**
	 * Adds a new message into the database, from the given employee.
	 *
	 * @param employee the employee that posted the message.
	 * @param message  the message to save.
	 * @return a boolean indicating the success of the operation.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static boolean addMessage(Employee employee, String message)
			throws SQLException, IOException, ClassNotFoundException {

		return addMessage(employee.getID(), message);
	}

	/**
	 * Adds a new message into the database, from the given employee.
	 *
	 * @param employee the employee that posted the message.
	 * @param message  the message to save.
	 * @return a boolean indicating the success of the operation.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static boolean addMessage(String employee, String message)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("communication.add"));
			statement.setString(1, employee);
			statement.setString(2, message);

			return statement.executeUpdate() == 1;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}

	/**
	 * Closes all open connections when done with SQL Query
	 *
	 * @param connection
	 * @param statement
	 * @param results
	 */
	private static void closeConnections(Connection connection, PreparedStatement statement, ResultSet results) {
		try {
			if (results != null) results.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		} catch (SQLException ignored) {

		}
	}
}
