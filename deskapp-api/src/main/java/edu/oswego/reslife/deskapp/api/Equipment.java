package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.RentedEquipmentRecord;
import edu.oswego.reslife.deskapp.api.models.Status;
import edu.oswego.reslife.deskapp.api.sql.SQLConnection;
import edu.oswego.reslife.deskapp.api.sql.SQLQueryManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.oswego.reslife.deskapp.utils.StaticUtils.closeConnections;

public class Equipment {

	/**
	 * Finds all equipment that are currently rented out for a given building.
	 *
	 * @param buildingID the building to search rented out equipment for.
	 * @return an array of records of rented out equipment.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static RentedEquipmentRecord[] listRentedOut(String buildingID) throws SQLException, IOException, ClassNotFoundException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("equipment.rentedOut"));
			statement.setString(1, buildingID);
			results = statement.executeQuery();

			ArrayList<RentedEquipmentRecord> records = new ArrayList<>();

			while (results.next()) {
				RentedEquipmentRecord record = new RentedEquipmentRecord();

				record.rentID = results.getString("Rent_ID");
				record.timeOut = results.getTimestamp("Time_Out");

				RentedEquipmentRecord.Equipment equipment = new RentedEquipmentRecord.Equipment();
				equipment.id = results.getString("Equipment_ID");
				equipment.name = results.getString("Equipment_Name");
				record.equipment = equipment;

				RentedEquipmentRecord.Resident resident = new RentedEquipmentRecord.Resident();
				resident.id = results.getString("Resident_ID");
				resident.firstName = results.getString("Resident_First_Name");
				resident.lastName = results.getString("Resident_Last_Name");
				record.resident = resident;

				RentedEquipmentRecord.Employee employee = new RentedEquipmentRecord.Employee();
				employee.id = results.getString("Employee_ID");
				employee.firstName = results.getString("Employee_First_Name");
				employee.lastName = results.getString("Employee_Last_Name");
				record.employee = employee;

				records.add(record);
			}

			RentedEquipmentRecord[] ret = new RentedEquipmentRecord[records.size()];
			records.toArray(ret);
			return ret;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}

	public static void log(String residentID, String equipmentID, Employee employee)
			throws SQLException, IOException, ClassNotFoundException {

		log(residentID, equipmentID, employee.getID());
	}

	/**
	 * Logs in / out a given equipment.
	 *
	 * @param residentID  The resident that logged in / out the item. Can be null if logged some equipment back in.
	 * @param equipmentID The equipment that is to be logged in / out.
	 * @param employeeID  The employee responsible for the transaction.
	 * @return the new status of the equipment.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	public static Status log(String residentID, String equipmentID, String employeeID)
			throws SQLException, IOException, ClassNotFoundException {

		String rentID = checkIfRentedOut(equipmentID);

		if (residentID == null) {
			// we don't need the resident ID when just logging something back in.
			logIn(rentID, employeeID);
			return Status.LOGGED_IN;
		}

		if (rentID == null) {
			// The equipment is not currently logged out.
			logOut(residentID, equipmentID, employeeID);
			return Status.LOGGED_OUT;
		}

		// We need to log the equipment back in.
		logIn(rentID, employeeID);
		return Status.LOGGED_IN;
	}

	/**
	 * Logs a given equipment out to a given resident.
	 *
	 * @param residentID  The resident that logged out the item.
	 * @param equipmentID The equipment that is to be logged out.
	 * @param employeeID  The employee responsible for the transaction.
	 * @return success if the log out was successful.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	private static boolean logOut(String residentID, String equipmentID, String employeeID)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("equipment.logOut"));
			statement.setString(1, equipmentID);
			statement.setString(2, residentID);
			statement.setString(3, employeeID);

			return statement.executeUpdate() == 1;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}

	/**
	 * Logs a given equipment back in.
	 *
	 * @param rentID     the ID the the rent transaction.
	 * @param employeeID The employee responsible for the transaction.
	 * @return success if the log in was successful.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	private static boolean logIn(String rentID, String employeeID)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("equipment.logIn"));
			statement.setString(1, employeeID);
			statement.setString(2, rentID);

			return statement.executeUpdate() == 1;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, null);
		}
	}

	/**
	 * Checks if a given equipment is currently logged out.
	 * Returns the ID of the Rent Record if the equipment, or null if the equipment is not logged out.
	 *
	 * @param equipmentID The equipment ID to check for.
	 * @return the ID of the Rent Record, or null if the item is not currently logged out.
	 * @throws SQLException           if there was a problem connection with the database or executing the query.
	 * @throws IOException            if there was a problem accessing the local disk.
	 * @throws ClassNotFoundException if there was a problem loading the JBDC driver.
	 */
	private static String checkIfRentedOut(String equipmentID)
			throws SQLException, IOException, ClassNotFoundException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {
			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("equipment.isRentedOut"));
			statement.setString(1, equipmentID);

			results = statement.executeQuery();
			if (!results.next()) {
				return null;
			}

			return results.getString("ID");

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw e;
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}
}
