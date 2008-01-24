package com.example.entity;

import org.seasar.dao.annotation.tiger.Id;
import org.seasar.dao.annotation.tiger.IdType;


public class Employee {
    private long employeeId;
    private String employeeName;
    
    @Id(IdType.IDENTITY)
    public long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(long id) {
        this.employeeId = id;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String name) {
        this.employeeName = name;
    }
}
