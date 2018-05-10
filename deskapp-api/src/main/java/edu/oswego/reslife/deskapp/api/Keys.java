package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Access;;
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
				record.employee_out = employee;

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

	/**
	 * Finds all keys for a given building.
	 *
	 * @param buildingID the building to search keys for.
	 * @return an array of access items.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	public static Access[] list(String buildingID) throws TransactionException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.list"));
			statement.setString(1, buildingID);
			results = statement.executeQuery();

			ArrayList<Access> records = new ArrayList<>();

			while (results.next()) {
				Access record = new Access();

				record.setID(results.getString("ID"));
				record.setBuilding(results.getString("Building_ID"));
				record.setType(results.getString("Type"));

				records.add(record);
			}

			Access[] ret = new Access[records.size()];
			records.toArray(ret);
			return ret;

		} catch (IOException | SQLException | ClassNotFoundException e) {
			throw new TransactionException(e);
		} finally {
			// Close all connections
			closeConnections(connection, statement, results);
		}
	}

	/**
	 * Returns a history of transactions for a given key.
	 *
	 * @param accessID The equipment to look up.
	 * @return An array of RentedAccessRecords.
	 * @throws TransactionException if there was any problem completing the transaction.
	 */
	public static RentedAccessRecord[] history(String accessID) throws TransactionException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			connection = SQLConnection.getSQLConnection();
			SQLQueryManager manager = SQLConnection.getManager();

			statement = connection.prepareStatement(manager.getSQLQuery("keys.history"));
			statement.setString(1, accessID);
			results = statement.executeQuery();

			ArrayList<RentedAccessRecord> records = new ArrayList<>();

			while (results.next()) {
				RentedAccessRecord record = new RentedAccessRecord();

				record.rentID = results.getString("Rent_ID");
				record.timeOut = results.getTimestamp("Time_Out");
				record.timeIn = results.getTimestamp("Time_In");

				RentedAccessRecord.Access equipment = new RentedAccessRecord.Access();
				equipment.id = results.getString("Access_ID");
				equipment.type = results.getString("Access_type");
				record.access = equipment;

				RentedAccessRecord.Resident resident = new RentedAccessRecord.Resident();
				resident.id = results.getString("Resident_ID");
				resident.firstName = results.getString("Resident_First_Name");
				resident.lastName = results.getString("Resident_Last_Name");
				record.resident = resident;

				RentedAccessRecord.Employee employee_out = new RentedAccessRecord.Employee();
				employee_out.id = results.getString("Employee_Out_ID");
				employee_out.firstName = results.getString("Employee_Out_First_Name");
				employee_out.lastName = results.getString("Employee_Out_Last_Name");
				record.employee_out = employee_out;

				RentedAccessRecord.Employee employee_in = new RentedAccessRecord.Employee();
				employee_in.id = results.getString("Employee_In_ID");
				employee_in.firstName = results.getString("Employee_In_First_Name");
				employee_in.lastName = results.getString("Employee_In_Last_Name");
				record.employee_in = employee_in;

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
	 * @param employeeID  The employee_out responsible for the transaction.
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
	 * @param employeeID  The employee_out responsible for the transaction.
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
	 * @param employeeID The employee_out responsible for the transaction.
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

	/**
     * Creates a new access in the database.
     *
     * @param access the iem to create.
     * @return true if the transaction was successful (i.e. there was no
     * access with that ID).
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean create(Access access)
            throws TransactionException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("keys.create"));
            statement.setString(1, access.getID());
			statement.setString(2, access.getBuilding());
			statement.setString(3, access.getType().name().toLowerCase());

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Deletes a given access from the database.
     *
     * @param accessID The access to delete.
     * @return true if the access was successfully deleted.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean delete(String accessID) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("keys.delete"));
			statement.setString(1, accessID);
			statement.setString(2, accessID);

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
	}
}
