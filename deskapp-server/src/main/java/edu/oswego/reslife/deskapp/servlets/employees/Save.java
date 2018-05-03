package edu.oswego.reslife.deskapp.servlets.employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Users;
import edu.oswego.reslife.deskapp.api.models.Employee;
import edu.oswego.reslife.deskapp.servlets.requests.EmployeeSaveRequest;
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
        EmployeeSaveRequest data = mapper.readValue(request.getReader(), EmployeeSaveRequest.class);

        Employee employee = data.getEmployee();
        String id = data.getID();
        employee.setBuilding(user.getBuilding());

        try {

            if (!user.getPosition().equals(Employee.Position.RHD) && !user.getPosition().equals(Employee.Position.AHD)) {
                throw new TransactionException("You do not have the right to perform this operation.");
            }
            
            if (id != null && Users.exists(id) && !Users.update(id, employee)) {
                throw new TransactionException("There was an error updating that user.");
            } else if (!Users.create(employee)) {
                throw new TransactionException("There was an error creating that user.");
            }

        } catch (TransactionException e) {
            e.writeMessageAsJson(response.getOutputStream());
        }
    }

    // We do not accept any other type of request than POST.
}
