package edu.oswego.reslife.deskapp.servlets.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Keys;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Status;
import edu.oswego.reslife.deskapp.servlets.requests.AccessLogRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Log extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		AccessLogRequest req = mapper.readValue(request.getReader(), AccessLogRequest.class);
		Employee employee = (Employee) request.getSession().getAttribute("user");

		try {
			Status status = Keys.log(req.getResidentID(), req.getAccessID(), employee.getID());
			System.out.printf("Equipment # '%s' is now marked as %s.%n", req.getAccessID(), status);
		} catch (TransactionException e) {
			e.writeMessageAsJson(System.out);
			e.writeMessageAsJson(response.getOutputStream());
		}
	}
}
