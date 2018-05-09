package edu.oswego.reslife.deskapp.servlets.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Keys;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Access;
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
        Access data = mapper.readValue(request.getReader(), Access.class);

        try {

            if (!user.getPosition().equals(Employee.Position.RHD)
                    && !user.getPosition().equals(Employee.Position.AHD)) {
                throw new TransactionException("You do not have the right to perform this operation.");
            }

            data.setBuilding(user.getBuilding());
            if (!Keys.create(data)) {
                throw new TransactionException("There was an error creating that access.");
            }

            System.out.printf("Access '%s' was created.%n", data.getID());

            response.getWriter().println("{}");

        } catch (TransactionException e) {
            e.writeMessageAsJson(response.getOutputStream());
        }
    }
}
