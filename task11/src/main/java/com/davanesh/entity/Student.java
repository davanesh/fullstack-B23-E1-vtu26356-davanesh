package com.davanesh.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "students_task11")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_name", nullable = false)
    private String name;

    @Column(name = "department")
    private String department;

    @Column(name = "age")
    private Integer age;

    // Constructors
    public Student() {}

    public Student(String name, String department, Integer age) {
        this.name = name;
        this.department = department;
        this.age = age;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
