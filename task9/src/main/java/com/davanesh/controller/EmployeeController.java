package com.davanesh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.davanesh.model.Employee;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    @GetMapping("/employees")
    public String showEmployees(Model model) {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee(101, "Jane Doe", "IT"));
        list.add(new Employee(102, "John Smith", "Marketing"));
        
        model.addAttribute("employeeList", list);
        return "employee"; // resolves to /WEB-INF/views/employee.jsp
    }
}
