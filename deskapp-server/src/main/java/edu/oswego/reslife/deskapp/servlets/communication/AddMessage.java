package edu.oswego.reslife.deskapp.servlets.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Communication;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Message;
import edu.oswego.reslife.deskapp.servlets.requests.AddMessageRequest;
import edu.oswego.reslife.deskapp.servlets.requests.ListMessagesRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddMessage extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		String message = mapper.readValue(request.getReader(), AddMessageRequest.class).getMessage();
		Employee employee = (Employee) request.getSession().getAttribute("user");

		try {
			Communication.addMessage(employee, message);
			System.out.printf("'%s %s' from '%s' says:%n%s%n", employee.getFirstName(), employee.getLastName(), employee.getBuilding(), message);
		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}

	// We do not accept any other type of request than POST.
}
