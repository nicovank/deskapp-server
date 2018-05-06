package edu.oswego.reslife.deskapp.servlets.residents;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Residents;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.api.models.Resident;
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
        Resident data = mapper.readValue(request.getReader(), Resident.class);

        try {

            if (!user.getPosition().equals(Employee.Position.RHD) && !user.getPosition().equals(Employee.Position.AHD)) {
                throw new TransactionException("You do not have the right to perform this operation.");
            }

            if (data.getID() != null && Residents.exists(data.getID())) {
                if (!Residents.update(data)) {
                    throw new TransactionException("There was an error updating that resident.");
                }
                System.out.printf("Resident '%s %s''s information was successfully updated.%n", data.getFirstName(), data.getLastName());
            } else {
                if (!Residents.create(data)) {
                    throw new TransactionException("There was an error creating that resident.");
                }

                System.out.printf("Resident '%s %s' was created in '%s'.", data.getFirstName(), data.getLastName(), data.getBuilding());
            }

            response.getWriter().println("{}");

        } catch (TransactionException e) {
            e.writeMessageAsJson(response.getOutputStream());
        }
    }
}
