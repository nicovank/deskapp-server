package edu.oswego.reslife.deskapp.servlets.equipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Equipment;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.RentedEquipmentRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ListRentedOutEquipment extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee employee = (Employee) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();

		try {

			RentedEquipmentRecord[] records = Equipment.listRentedOut(employee.getBuilding());
			mapper.writeValue(response.getOutputStream(), records);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException("There was an error connecting to the database.", e);
		}
	}
}
