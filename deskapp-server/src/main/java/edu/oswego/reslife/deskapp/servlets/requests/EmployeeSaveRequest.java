package edu.oswego.reslife.deskapp.servlets.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.oswego.reslife.deskapp.api.models.Employee;

public class EmployeeSaveRequest {
    private String id;
    private Employee employee;
    
    @JsonProperty("id")
    public String getID() {
        return id;
    }
    
    @JsonProperty("employee")
    public Employee getEmployee() {
        return employee;
    }
}
