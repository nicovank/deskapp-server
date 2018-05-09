package edu.oswego.reslife.deskapp.servlets.equipment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Equipment;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.EquipmentModel;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Save extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee user = ((Employee) request.getSession().getAttribute("user"));

		ObjectMapper mapper = new ObjectMapper();
		EquipmentModel data = mapper.readValue(request.getReader(), EquipmentModel.class);

		try {

			if (!user.getPosition().equals(Employee.Position.RHD) && !user.getPosition().equals(Employee.Position.AHD)) {
				throw new TransactionException("You do not have the right to perform this operation.");
			}

			if (data.getID() != null && Equipment.exists(data.getID())) {
				if (!Equipment.update(data)) {
					throw new TransactionException("There was an error updating this equipment.");
				}
				System.out.printf("Equipment '%s''s information was successfully updated.%n", data.getID());
			} else {
				data.setBuilding(user.getBuilding());
				if (!Equipment.create(data)) {
					throw new TransactionException("There was an error creating that equipment.");
				}

				System.out.printf("Equipment '%s' was created.%n", data.getID());
			}

			response.getWriter().println("{}");

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}
}
