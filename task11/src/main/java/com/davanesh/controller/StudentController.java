package com.davanesh.controller;

import com.davanesh.entity.Student;
import com.davanesh.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students/search")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Get strictly by department
    @GetMapping("/by-dept")
    public List<Student> getByDepartment(@RequestParam String dept) {
        return studentRepository.findByDepartment(dept);
    }

    // Get with minimum age and sorted by name
    @GetMapping("/older-than")
    public List<Student> getOlderThan(@RequestParam Integer age) {
        Sort sortByName = Sort.by(Sort.Direction.ASC, "name");
        return studentRepository.findByAgeGreaterThan(age, sortByName);
    }

    // Paginated and sorted response
    @GetMapping("/paged")
    public Page<Student> getPagedStudents(
            @RequestParam String dept, 
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("name").ascending());
        return studentRepository.findByDepartment(dept, pageRequest);
    }
}
