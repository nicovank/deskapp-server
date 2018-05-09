package edu.oswego.reslife.deskapp.servlets.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Keys;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.servlets.requests.SingleIDRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Delete extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee employee = (Employee) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();
		String id = mapper.readValue(request.getInputStream(), SingleIDRequest.class).getID();

		try {

			if (!employee.getPosition().equals(Employee.Position.RHD) && !employee.getPosition().equals(Employee.Position.AHD)) {
				throw new TransactionException("You do not have the right to perform this operation.");
			}

			if (!Keys.delete(id)) {
				throw new TransactionException("There was an error deleting the key.");
			}

			response.getWriter().println("{}");
			System.out.printf("Key '%s' was successfully deleted from the system.%n", id);

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}
}
