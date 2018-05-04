package edu.oswego.reslife.deskapp.api;

import edu.oswego.reslife.deskapp.api.models.Employee;
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

import static edu.oswego.reslife.deskapp.utils.StaticUtils.*;

public class Users {

    /**
     * Checks if a user's ID exists in the database.
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

            statement = connection.prepareStatement(manager.getSQLQuery("employees.exists"));
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
     * Checks if a user / password pair exists in the database.
     *
     * @param emailOrID The email or ID to look up.
     * @param password The password entered by the user.
     * @return the logged in employee_out, or null if the password or email/ID is
     * incorrect.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static Employee login(String emailOrID, String password)
            throws TransactionException {

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
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, results);
        }
    }

    /**
     * Creates a new user in the database.
     *
     * @param employee the employee_out to create.
     * @return true if the transaction was successful (i.e. there was no
     * employee_out with that ID or email already).
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean create(Employee employee)
            throws TransactionException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            String hashed = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt());

            statement = connection.prepareStatement(manager.getSQLQuery("employees.create"));
            statement.setString(1, employee.getID());
            statement.setString(2, employee.getBuilding());
            statement.setString(3, employee.getFirstName());
            statement.setString(4, employee.getLastName());
            statement.setString(5, employee.getPosition().name());
            statement.setString(6, employee.getEmail());
            statement.setString(7, hashed);
            statement.setString(8, employee.getPhoneNb());

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Updates the existing user that has the given id.
     *
     * @param id The id of the existing user.
     * @param employee The information to override the current record with.
     * @return true if the transaction was successful, else false.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean update(Employee employee)
            throws TransactionException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("employees.update"));
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getPosition().name());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getPhoneNb());
            statement.setString(6, employee.getID());

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Deletes a given employee_out from the database.
     *
     * @param employeeID The employee_out to delete.
     * @return true if the employee_out was successfully deleted.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static boolean delete(String employeeID) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {

            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("employees.delete"));
            statement.setString(1, employeeID);

            return statement.executeUpdate() == 1;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, null);
        }
    }

    /**
     * Lists all employees for a given building.
     *
     * @param buildingID The building ID to search for.
     * @return a list of employees registered at the given building.
     * @throws TransactionException if there was any problem completing the
     * transaction.
     */
    public static Employee[] list(String buildingID) throws TransactionException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet results = null;

        try {

            connection = SQLConnection.getSQLConnection();
            SQLQueryManager manager = SQLConnection.getManager();

            statement = connection.prepareStatement(manager.getSQLQuery("employees.list"));
            statement.setString(1, buildingID);

            results = statement.executeQuery();

            ArrayList<Employee> employees = new ArrayList<>();

            while (results.next()) {
                Employee employee = new Employee();

                employee.setID(results.getString("ID"));
                employee.setFirstName(results.getString("First_Name"));
                employee.setLastName(results.getString("Last_Name"));
                employee.setPosition(results.getString("Position"));
                employee.setEmail(results.getString("Email"));
                employee.setPhoneNb(results.getString("Phone_Num"));

                employees.add(employee);
            }

            Employee[] ret = new Employee[employees.size()];
            employees.toArray(ret);
            return ret;

        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new TransactionException(e);
        } finally {
            // Close all connections
            closeConnections(connection, statement, results);
        }
    }
}
