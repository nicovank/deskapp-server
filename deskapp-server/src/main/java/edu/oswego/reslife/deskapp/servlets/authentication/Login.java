package edu.oswego.reslife.deskapp.servlets.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Users;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

public class Login extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ObjectMapper mapper = new ObjectMapper();

		try {
			Employee employee = Users.login(request.getParameter("user"), request.getParameter("password"));

			if (employee != null) {
				String token = UUID.randomUUID().toString();

				request.getSession().setAttribute("user", employee);
				request.getSession().setAttribute("token", token);

				response.addCookie(new Cookie("token", token));
				response.addCookie(new Cookie("user", mapper.writeValueAsString(employee)));

				response.sendRedirect("/staff/build/");

				return;
			} else {
				request.setAttribute("error", "There was an error logging in. Email or password incorrect.");
			}
		} catch (TransactionException e) {
			request.setAttribute("error", "There was an error with the server. If the error persists, please contact your supervisor.");
		}

		request.getRequestDispatcher("/login/index.jsp").forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/login/index.jsp").forward(request, response);
	}
}
