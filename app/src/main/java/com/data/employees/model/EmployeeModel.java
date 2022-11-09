package com.data.employees.model;

public class EmployeeModel {
    int EmpID;
    String EmpName;

    public EmployeeModel(){}

    public EmployeeModel(int empID, String empName) {
        EmpID = empID;
        EmpName = empName;
    }

    public int getEmpID() {
        return EmpID;
    }

    public void setEmpID(int empID) {
        EmpID = empID;
    }

    public String getEmpName() {
        return EmpName;
    }

    public void setEmpName(String empName) {
        EmpName = empName;
    }
}
