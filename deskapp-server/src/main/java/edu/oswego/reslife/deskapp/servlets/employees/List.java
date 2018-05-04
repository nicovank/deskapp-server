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

public class List extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee employee = (Employee) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();

		try {

			if (!employee.getPosition().equals(Employee.Position.RHD) && !employee.getPosition().equals(Employee.Position.AHD)) {
				throw new TransactionException("You do not have the right to perform this operation.");
			}

			Employee[] records = Users.list(employee.getBuilding());
			mapper.writeValue(response.getOutputStream(), records);
			System.out.printf("There are %d employees in '%s'.%n", records.length, employee.getBuilding());

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}
