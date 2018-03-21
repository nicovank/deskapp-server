package edu.oswego.reslife.deskapp.servlets.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Communication;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Message;
import edu.oswego.reslife.deskapp.servlets.requests.ListMessagesRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class AddMessage extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			Employee e = new Employee();
			e.setID("804982890");
			String message = "ya yeet this works perfectly sushi. I have an IQ over 9000.";

			Communication.addMessage(e, message);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new ServletException("There was an error connecting to the database.", e);
		}
	}

	// We do not accept any other type of request than POST.
}
