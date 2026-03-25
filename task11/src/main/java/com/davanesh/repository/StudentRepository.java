package com.davanesh.repository;

import com.davanesh.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Custom query method based on department
    List<Student> findByDepartment(String department);

    // Custom query method based on age with Sort
    List<Student> findByAgeGreaterThan(Integer age, Sort sort);

    // Pagination example
    Page<Student> findByDepartment(String department, Pageable pageable);

    // Custom JPQL Query example
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:keyword%")
    List<Student> searchByNameLike(String keyword);
}
