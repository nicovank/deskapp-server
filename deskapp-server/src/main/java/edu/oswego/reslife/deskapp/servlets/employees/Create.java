package edu.oswego.reslife.deskapp.servlets.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Users;
import edu.oswego.reslife.deskapp.api.models.Employee;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Create extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		Employee employee = mapper.readValue(request.getReader(), Employee.class);
		employee.setBuilding("MACKIN");
		// employee.setBuilding(((Employee) request.getSession().getAttribute("user")).getBuilding());

		try {

			if(Users.create(employee)) {
				response.getWriter().println("{}");
			} else {
				mapper.writeValue(response.getOutputStream(), new Object() {
					String error = "it totally failed.";
				});
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException("There was an error connecting to the database.", e);
		}
	}

	// We do not accept any other type of request than POST.
}
