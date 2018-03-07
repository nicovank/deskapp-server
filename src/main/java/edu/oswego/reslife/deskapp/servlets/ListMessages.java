package edu.oswego.reslife.deskapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Communication;
import edu.oswego.reslife.deskapp.api.sql.models.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ListMessages extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			Message[] messages = Communication.listMessages(0, "MACKIN");
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), messages);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
