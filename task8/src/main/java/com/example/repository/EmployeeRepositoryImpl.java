package com.example.repository;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import com.example.model.Employee;

@Component
public class EmployeeRepositoryImpl {
    private List<Employee> employees = new ArrayList<>();

    public void save(Employee employee) {
        employees.add(employee);
        System.out.println("Employee stored in memory: " + employee);
    }

    public List<Employee> findAll() {
        return employees;
    }
}
