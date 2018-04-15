package edu.oswego.reslife.deskapp.servlets.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Users;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Create extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee user = ((Employee) request.getSession().getAttribute("user"));

		if (!user.getPosition().equals(Employee.Position.RHD) || !user.getPosition().equals(Employee.Position.AHD)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		ObjectMapper mapper = new ObjectMapper();
		Employee employee = mapper.readValue(request.getReader(), Employee.class);
		employee.setBuilding(user.getBuilding());

		try {

			if(!Users.create(employee)) {
				throw new ServletException("There was an error creating that user. The email or ID number is already in the database.");
			}

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}

	// We do not accept any other type of request than POST.
}
