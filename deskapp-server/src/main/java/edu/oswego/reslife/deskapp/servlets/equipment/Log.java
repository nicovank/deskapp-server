package edu.oswego.reslife.deskapp.servlets.equipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Equipment;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Status;
import edu.oswego.reslife.deskapp.servlets.requests.LogRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class Log extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		LogRequest req = mapper.readValue(request.getReader(), LogRequest.class);
		Employee employee = (Employee) request.getSession().getAttribute("user");

		try {
			Status status = Equipment.log(req.getResidentID(), req.getEquipmentID(), employee.getID());
		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}
}
