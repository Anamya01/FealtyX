package com.example.FealtyX.Service.Implementation;

import com.example.FealtyX.DTO.StudentDTO;
import com.example.FealtyX.Entity.Student;
import com.example.FealtyX.Repository.StudentRepository;
import com.example.FealtyX.Service.Interface.StudentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StudentServiceImpl(StudentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Student createStudent(StudentDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());
        return repository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @Override
    public Student updateStudent(Long id, StudentDTO dto) {
        Student student = getStudentById(id);
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setEmail(dto.getEmail());
        return repository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }

    @Override
    public String getStudentSummary(Long id) {
        Student student = getStudentById(id);

        String prompt = String.format(
                "Summarize the following student's profile:\nName: %s\nAge: %d\nEmail: %s",
                student.getName(), student.getAge(), student.getEmail()
        );

        String url = "http://localhost:11434/api/generate";

        var requestBody = new java.util.HashMap<String, Object>();
        requestBody.put("model", "llama3");
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);

        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Content-Type", "application/json");

        var entity = new org.springframework.http.HttpEntity<>(requestBody, headers);
        var response = restTemplate.postForEntity(url, entity, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String summary = jsonNode.get("response").asText();
            return summary.replace("\\n", "\n").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing summary response.";
        }
    }
}
