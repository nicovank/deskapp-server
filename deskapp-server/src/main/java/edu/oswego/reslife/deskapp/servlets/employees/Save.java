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
            } else {
                // Generate password maybe
                data.setBuilding(user.getBuilding());
                if (!Users.create(data)) {
                    throw new TransactionException("There was an error creating that user.");
                }
            }

            response.getWriter().println("{}");

        } catch (TransactionException e) {
            e.writeMessageAsJson(response.getOutputStream());
        }
    }

    // We do not accept any other type of request than POST.
}
