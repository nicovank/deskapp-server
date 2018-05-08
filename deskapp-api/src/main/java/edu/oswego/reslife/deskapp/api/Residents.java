package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Resident;
import edu.oswego.reslife.deskapp.api.sql.SQLConnection;
import edu.oswego.reslife.deskapp.api.sql.SQLQueryManager;
import edu.oswego.reslife.deskapp.utils.TransactionException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static edu.oswego.reslife.deskapp.utils.StaticUtils.closeConnections;

public class Residents {

    /**
     * Checks if a residents's ID exists in the database.
     *
     * @param id The ID to check for.
     * @return true if the ID already exists, else false.
     * @throws TransactionException TransactionException if there was any
     * problem completing the transaction.
     */
    public static boolean exists(String id) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("residents.exists"));
            statement.setString(1, id);

            results = statement.executeQuery();
            return results.next();

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, results);
        }
    }

    /**
     * Creates a new resident in the database.
     *
     * @param resident the resident to create.
     * @return true if the transaction was successful (i.e. there was no
     * resident with that ID or email already).
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean create(Resident resident)
            throws TransactionException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("residents.create"));
            statement.setString(1, resident.getID());
            statement.setString(2, resident.getFirstName());
            statement.setString(3, resident.getLastName());
            statement.setString(4, resident.getBuilding());
            statement.setString(5, resident.getRoomNb());
            statement.setString(6, resident.getEmail());

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Updates an existing resident.
     *
     * @param resident The information to override the current record with.
     * @return true if the transaction was successful, else false.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean update(Resident resident)
            throws TransactionException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("residents.update"));
            statement.setString(1, resident.getFirstName());
            statement.setString(2, resident.getLastName());
            statement.setString(3, resident.getBuilding());
            statement.setString(4, resident.getRoomNb());
            statement.setString(5, resident.getEmail());
            statement.setString(6, resident.getID());

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Deletes a given resident from the database.
     *
     * @param residentID The resident to delete.
     * @return true if the resident was successfully deleted.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean delete(String residentID) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("residents.delete"));
            statement.setString(1, residentID);

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Lists all residents for a given building.
     *
     * @param buildingID The building ID to search for.
     * @return a list of residents registered at the given building.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static Resident[] list(String buildingID) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {

            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("residents.list"));
            statement.setString(1, buildingID);

            results = statement.executeQuery();

            ArrayList<Resident> residents = new ArrayList<>();

            while (results.next()) {
                Resident resident = new Resident();

                resident.setID(results.getString("ID"));
                resident.setFirstName(results.getString("First_Name"));
                resident.setLastName(results.getString("Last_Name"));
                resident.setBuilding(results.getString("Building_ID"));
                resident.setEmail(results.getString("Email"));
                resident.setRoomNb(results.getString("Room_Num"));

                residents.add(resident);
            }

            Resident[] ret = new Resident[residents.size()];
            residents.toArray(ret);
            return ret;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, results);
        }
    }
}
