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
import java.util.Random;

public class Save extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Employee user = ((Employee) request.getSession().getAttribute("user"));

		ObjectMapper mapper = new ObjectMapper();
		Employee data = mapper.readValue(request.getReader(), Employee.class);

		try {

			if (!user.getPosition().equals(Employee.Position.RHD) && !user.getPosition().equals(Employee.Position.AHD)) {
				throw new TransactionException("You do not have the right to perform this operation.");
			}

			if (data.getID() != null && Users.exists(data.getID())) {
				if (!Users.update(data)) {
					throw new TransactionException("There was an error updating that user.");
				}
				System.out.printf("Employee '%s %s''s information was successfully updated.%n", data.getFirstName(), data.getLastName());
			} else {
				String password = randomPassword(8);
				data.setPassword(password);
				data.setBuilding(user.getBuilding());
				if (!Users.create(data)) {
					throw new TransactionException("There was an error creating that user.");
				}

				System.out.printf("Employee '%s %s' was created with password '%s'.%n", data.getFirstName(), data.getLastName(), password);
			}

			response.getWriter().println("{}");

		} catch (TransactionException e) {
			e.writeMessageAsJson(response.getOutputStream());
		}
	}

	/**
	 * Generates a random password of a given length.
	 *
	 * @param length the length of the password.
	 * @return a randomly-generated password.
	 */
	private static String randomPassword(int length) {
		String possibilities = "ABCDEFGHIJKLMNOPQRSTUVWXYVabcdefghijklmnopqrstuvwxyz0123456789";

		StringBuilder sb = new StringBuilder(length);
		Random r = new Random();

		for (int i = 0; i < length; ++i) {
			sb.append(possibilities.charAt(r.nextInt(possibilities.length())));
		}

		return sb.toString();
	}

	// We do not accept any other type of request than POST.
}
