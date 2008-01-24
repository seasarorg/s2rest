package com.example.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProduceMime;
import javax.ws.rs.PathParam;

import com.example.entity.Employee;

public interface Jsr311MethodLevelPathService {
    
    @GET
    @Path("employeePath")
    @ProduceMime({"application/json","application/atom+xml","text/xml"})
    public List<Employee> getAll();
    
    @GET
    @Path("employeePath/{employeeId}")
    @ProduceMime({"application/json","application/atom+xml","text/xml","application/xhtml+xml","text/html"})
    public Employee findById(@PathParam("employeeId") long id);
}
