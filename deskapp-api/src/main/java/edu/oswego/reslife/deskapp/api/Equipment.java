package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.RentedEquipmentRecord;
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

	public static void log(String residentID, String equipmentID, Employee employee) {
		log(residentID, equipmentID, employee.getID());
	}

	public static void log(String residentID, String equipmentID, String employeeID) {
		// check if the item is currently logged out

	}
}
