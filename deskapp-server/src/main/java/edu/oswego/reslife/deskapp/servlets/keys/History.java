package edu.oswego.reslife.deskapp.servlets.keys;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.oswego.reslife.deskapp.api.Keys;
import edu.oswego.reslife.deskapp.api.models.RentedAccessRecord;
import edu.oswego.reslife.deskapp.servlets.requests.SingleIDRequest;
import edu.oswego.reslife.deskapp.utils.TransactionException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class History extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        String id = mapper.readValue(request.getInputStream(), SingleIDRequest.class).getID();

        try {

            RentedAccessRecord[] records = Keys.history(id);
            mapper.writeValue(response.getOutputStream(), records);
            System.out.printf("There are %d records for the key '%s'.%n", records.length, records[0].access.id);

        } catch (TransactionException e) {
            e.writeMessageAsJson(response.getOutputStream());
        }
    }
}
