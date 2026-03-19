package com.learn.project.md3_ss12.service;

import com.learn.project.md3_ss12.dto.EmployeeCreateDTO;
import com.learn.project.md3_ss12.entity.Employee;
import com.learn.project.md3_ss12.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee createEmployee(EmployeeCreateDTO dto) throws ResourceNotFoundException, IOException;
    Employee getEmployeeById(Long id);
}
