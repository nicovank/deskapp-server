package edu.oswego.reslife.deskapp.servlets;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.oswego.reslife.deskapp.api.Employees;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class ClientGlobalsServlet extends HttpServlet {

	private static final class GlobalsData {
		@JsonProperty("employee")
		Employee employee;

		@JsonProperty("token")
		String token;

		@JsonProperty("employees")
		Employee[] employees;

		public GlobalsData(Employee employee, String token, Employee[] employees) {
			this.employee = employee;
			this.token = token;
			this.employees = employees;
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		ObjectMapper mapper = new ObjectMapper();

		Employee employee = (Employee) request.getSession().getAttribute("user");
		String token = UUID.randomUUID().toString();
		request.getSession().setAttribute("token", token);

		try {
			Employee[] employees = Employees.list(employee.getBuilding());
			out.println("window.globals = ");
			mapper.writeValue(out, new GlobalsData(employee, token, employees));
		} catch (TransactionException e) {
			e.writeMessageAsJson(out);
		}

	}
}
