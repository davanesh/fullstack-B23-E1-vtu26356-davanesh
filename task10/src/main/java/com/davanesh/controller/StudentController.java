package com.davanesh.controller;

import com.davanesh.entity.Student;
import com.davanesh.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Create
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Read all
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Read by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Update
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        student.setName(studentDetails.getName());
        student.setDepartment(studentDetails.getDepartment());
        student.setAge(studentDetails.getAge());
        return studentRepository.save(student);
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "Student deleted with id: " + id;
    }
}
