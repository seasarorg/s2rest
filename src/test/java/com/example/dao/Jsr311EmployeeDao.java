package com.example.dao;

import java.util.List;

import javax.ws.rs.ConsumeMime;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;

import org.seasar.dao.annotation.tiger.Arguments;
import org.seasar.dao.annotation.tiger.S2Dao;

import com.example.entity.Employee;

@Path("employee")
@ConsumeMime( { "application/json", "application/x-www-form-urlencoded" })
@ProduceMime( { "application/json", "text/xml" })
@S2Dao(bean = Employee.class)
public interface Jsr311EmployeeDao {

	@GET
	@Path("list")
	public List<Employee> getAll();

	@GET
	@Path("{empId}")
	@Arguments("employeeId")
	public Employee getById(@PathParam("empId")
	long id);

	@POST
	@Path("list")
	public void insert(Employee employee);

	@PUT
	@Path("{empId}")
	public void update(Employee employee);

	@DELETE
	@Path("{empId}")
	public void delete(Employee employee);
}
