package edu.oswego.reslife.deskapp.servlets.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Communication;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Message;
import edu.oswego.reslife.deskapp.servlets.requests.ListMessagesRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ListMessages extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		ListMessagesRequest req = mapper.readValue(request.getReader(), ListMessagesRequest.class);

		try {

			Employee employee = (Employee) request.getSession().getAttribute("user");
			Message[] messages = Communication.listMessages(req.getPage(), employee.getBuilding());
			mapper.writeValue(response.getOutputStream(), messages);

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}

	// We do not accept any other type of request than POST.
}
