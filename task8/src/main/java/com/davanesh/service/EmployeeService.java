package com.davanesh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.davanesh.model.Employee;
import com.davanesh.repository.EmployeeRepositoryImpl;
import java.util.List;

@Component
public class EmployeeService {

    private final EmployeeRepositoryImpl repository;

    @Autowired
    public EmployeeService(EmployeeRepositoryImpl repository) {
        this.repository = repository;
    }

    public void addEmployee(int id, String name, String dept) {
        Employee employee = new Employee(id, name, dept);
        repository.save(employee);
    }

    public void displayAllEmployees() {
        List<Employee> list = repository.findAll();
        System.out.println("--- All Employees ---");
        for (Employee e : list) {
            System.out.println(e);
        }
    }
}
