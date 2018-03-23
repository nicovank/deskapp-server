package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.sql.SQLConnection;
import edu.oswego.reslife.deskapp.api.sql.SQLQueryManager;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static edu.oswego.reslife.deskapp.utils.StaticUtils.*;

public class Users {

	/**
	 * Checks if a user exists in the database.
	 *
	 * @param emailOrID The email or ID to look up.
	 * @param password  The password entered by the user.
	 * @return the logged in employee, or null if the password or email/ID is incorrect.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static Employee login(String emailOrID, String password)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("employees.findByEmailOrID"));
			statement.setString(1, emailOrID);
			statement.setString(2, emailOrID);

			results = statement.executeQuery();

			if (!results.next()) {
				return null;
			}

			String storedPassword = results.getString("Password");

			if (!BCrypt.checkpw(password, storedPassword)) {
				return null;
			}

			Employee employee = new Employee();

			employee.setID(results.getString("ID"));
			employee.setBuilding(results.getString("Building_ID"));
			employee.setFirstName(results.getString("First_Name"));
			employee.setLastName(results.getString("Last_Name"));
			employee.setPosition(results.getString("Position"));
			employee.setEmail(results.getString("Email"));
			employee.setPhoneNb(results.getString("Phone_Num"));

			return employee;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}

	/**
	 * Creates a new user in the database.
	 *
	 * @param employee the employee to create.
	 * @return true if the transaction was successful (i.e. there was no employee with that ID or email already).
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static boolean create(Employee employee)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("employees.create"));
			statement.setString(1, employee.getID());
			statement.setString(2, employee.getBuilding());
			statement.setString(3, employee.getFirstName());
			statement.setString(4, employee.getLastName());
			statement.setString(5, employee.getPosition().name());
			statement.setString(6, employee.getEmail());
			statement.setString(7, employee.getHashedPassword());
			statement.setString(8, employee.getPhoneNb());

			return statement.executeUpdate() == 1;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}
}
