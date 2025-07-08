package com.example.FealtyX.Repository;

import com.example.FealtyX.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
