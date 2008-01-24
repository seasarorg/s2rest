package com.example.logic;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;

import com.example.dao.Jsr311EmployeeDao;
import com.example.entity.Employee;

@Path("/empLogic/{employeeId}")
@ConsumeMime({"application/json","application/x-www-form-urlencoded"})
@ProduceMime({"application/json","text/xml"})
public class Jsr311EmployeeSearchLogic {
    private Jsr311EmployeeDao jsr311EmployeeDao;
    private long empId;
    
    public void setJsr311EmployeeDao(Jsr311EmployeeDao jsr311EmployeeDao) {
        this.jsr311EmployeeDao = jsr311EmployeeDao;
    }
    
    // changed on 0.6 refactor
//    @UriParam("employeeId")
    public void setEmpId(long empId) {
        this.empId = empId;
    }
    
    @GET
    public List<Employee> initialize() {
        return Arrays.asList(jsr311EmployeeDao.getById(this.empId));
    }
}
