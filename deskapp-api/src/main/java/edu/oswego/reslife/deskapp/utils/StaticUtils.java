package edu.oswego.reslife.deskapp.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaticUtils {

	/**
	 * Closes all open connections when done with SQL Query
	 *
	 * @param connection
	 * @param statement
	 * @param results
	 */
	public static void closeConnections(Connection connection, PreparedStatement statement, ResultSet results) {
		try {
			if (results != null) results.close();
			if (statement != null) statement.close();
			if (connection != null) connection.close();
		} catch (SQLException ignored) {

		}
	}
}
