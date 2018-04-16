package edu.oswego.reslife.deskapp.api;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.RentedAccessRecord;
import edu.oswego.reslife.deskapp.api.models.Status;
import edu.oswego.reslife.deskapp.api.sql.SQLConnection;
import edu.oswego.reslife.deskapp.api.sql.SQLQueryManager;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.oswego.reslife.deskapp.utils.StaticUtils.closeConnections;

public class Keys {

	/**
	 * Finds all keys that are currently rented out for a given building.
	 *
	 * @param buildingID the building to search rented out keys for.
	 * @return an array of records of rented out keys.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	public static RentedAccessRecord[] listRentedOut(String buildingID) throws TransactionException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.rentedOut"));
			statement.setString(1, buildingID);
			results = statement.executeQuery();

			ArrayList<RentedAccessRecord> records = new ArrayList<>();

			while (results.next()) {
				RentedAccessRecord record = new RentedAccessRecord();

				record.rentID = results.getString("Rent_ID");
				record.timeOut = results.getTimestamp("Time_Out");

				RentedAccessRecord.Access access = new RentedAccessRecord.Access();
				access.id = results.getString("Access_ID");
				access.type = results.getString("Access_Type");
				record.access = access;

				RentedAccessRecord.Resident resident = new RentedAccessRecord.Resident();
				resident.id = results.getString("Resident_ID");
				resident.firstName = results.getString("Resident_First_Name");
				resident.lastName = results.getString("Resident_Last_Name");
				record.resident = resident;

				RentedAccessRecord.Employee employee = new RentedAccessRecord.Employee();
				employee.id = results.getString("Employee_ID");
				employee.firstName = results.getString("Employee_First_Name");
				employee.lastName = results.getString("Employee_Last_Name");
				record.employee = employee;

				records.add(record);
			}

			RentedAccessRecord[] ret = new RentedAccessRecord[records.size()];
			records.toArray(ret);
			return ret;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new TransactionException(e);
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}

	public static void log(String residentID, String accessID, Employee employee)
			throws TransactionException {

		log(residentID, accessID, employee.getID());
	}

	/**
	 * Logs in / out a given key.
	 *
	 * @param residentID  The resident that logged in / out the key. Can be null if logging some key back in.
	 * @param accessID The key that is to be logged in / out.
	 * @param employeeID  The employee responsible for the transaction.
	 * @return the new status of the key.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	public static Status log(String residentID, String accessID, String employeeID)
			throws TransactionException {

		String rentID = checkIfRentedOut(accessID);

		if (rentID == null) {
			// The key is not currently logged out.
			logOut(residentID, accessID, employeeID);
			return Status.LOGGED_OUT;
		}

		// We need to log the key back in.
		logIn(rentID, employeeID);
		return Status.LOGGED_IN;
	}

	/**
	 * Logs a given key out to a given resident.
	 *
	 * @param residentID  The resident that logged out the key.
	 * @param accessID The key that is to be logged out.
	 * @param employeeID  The employee responsible for the transaction.
	 * @return success if the log out was successful.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	private static boolean logOut(String residentID, String accessID, String employeeID)
			throws TransactionException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.logOut"));
			statement.setString(1, accessID);
			statement.setString(2, residentID);
			statement.setString(3, employeeID);

			return statement.executeUpdate() == 1;

		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new TransactionException("Key or Resident ID could not be found.");
		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new TransactionException(e);
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}

	/**
	 * Logs a given key back in.
	 *
	 * @param rentID     the ID the the rent transaction.
	 * @param employeeID The employee responsible for the transaction.
	 * @return success if the log in was successful.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	private static boolean logIn(String rentID, String employeeID)
			throws TransactionException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.logIn"));
			statement.setString(1, employeeID);
			statement.setString(2, rentID);

			return statement.executeUpdate() == 1;

		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new TransactionException("Key or Resident ID could not be found.");
		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new TransactionException(e);
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}

	/**
	 * Checks if a given key is currently logged out.
	 * Returns the ID of the Rent Record if the key is logged ut, or null if the key is not logged out.
	 *
	 * @param accessID The key ID to check for.
	 * @return the ID of the Rent Record, or null if the key is not currently logged out.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	private static String checkIfRentedOut(String accessID)
			throws TransactionException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.isRentedOut"));
			statement.setString(1, accessID);

			results = statement.executeQuery();
			if (!results.next()) {
				return null;
			}

			return results.getString("ID");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new TransactionException(e);
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}
}
