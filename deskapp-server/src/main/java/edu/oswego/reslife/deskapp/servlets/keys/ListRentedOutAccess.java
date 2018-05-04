package edu.oswego.reslife.deskapp.servlets.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Keys;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.RentedAccessRecord;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListRentedOutAccess extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee employee = (Employee) request.getSession().getAttribute("user");
		ObjectMapper mapper = new ObjectMapper();

		try {

			RentedAccessRecord[] records = Keys.listRentedOut(employee.getBuilding());
			mapper.writeValue(response.getOutputStream(), records);

			System.out.printf("There are %d keys or fobs rented out in '%s'.%n", records.length, employee.getBuilding());

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
