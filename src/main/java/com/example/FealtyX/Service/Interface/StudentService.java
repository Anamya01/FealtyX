package com.example.FealtyX.Service.Interface;


import com.example.FealtyX.DTO.StudentDTO;
import com.example.FealtyX.Entity.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(StudentDTO studentDTO);
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
    String getStudentSummary(Long id);
}
