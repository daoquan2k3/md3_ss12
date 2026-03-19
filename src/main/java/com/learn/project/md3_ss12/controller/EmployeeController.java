package com.learn.project.md3_ss12.controller;

import com.learn.project.md3_ss12.config.CloudinaryConfig;
import com.learn.project.md3_ss12.dto.EmployeeCreateDTO;
import com.learn.project.md3_ss12.entity.Employee;
import com.learn.project.md3_ss12.exception.ResourceNotFoundException;
import com.learn.project.md3_ss12.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {
    private final EmployeeServiceImpl employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute EmployeeCreateDTO employeeCreateDTO) throws ResourceNotFoundException {
        try {
            Employee savedEmployee = employeeService.createEmployee(employeeCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Lỗi khi upload ảnh lên Cloudinary: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
}
