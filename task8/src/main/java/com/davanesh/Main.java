package com.davanesh;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import com.davanesh.config.AppConfig;
import com.davanesh.service.EmployeeService;

public class Main {
    public static void main(String[] args) {
        // Using BeanFactory (ApplicationContext is a sub-interface of BeanFactory)
        BeanFactory factory = new AnnotationConfigApplicationContext(AppConfig.class);
        
        EmployeeService service = factory.getBean(EmployeeService.class);
        
        // Add some dummy data
        service.addEmployee(1, "Alice Smith", "Engineering");
        service.addEmployee(2, "Bob Jones", "HR");
        service.addEmployee(3, "Charlie Brown", "Finance");

        // Display them
        service.displayAllEmployees();
    }
}
