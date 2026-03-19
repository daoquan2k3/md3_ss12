package com.learn.project.md3_ss12.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.learn.project.md3_ss12.dto.EmployeeCreateDTO;
import com.learn.project.md3_ss12.entity.Employee;
import com.learn.project.md3_ss12.exception.ResourceNotFoundException;
import com.learn.project.md3_ss12.repository.IEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final Cloudinary cloudinary;
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(EmployeeCreateDTO dto) throws ResourceNotFoundException, IOException {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceNotFoundException("Email đã tồn tại trong hệ thống!");
        }

        String avatarUrl = "";
        if (dto.getAvatarFile() != null && !dto.getAvatarFile().isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(
                    dto.getAvatarFile().getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            avatarUrl = (String) uploadResult.get("url");
        }

        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setAvatarUrl(avatarUrl);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));
    }
}
