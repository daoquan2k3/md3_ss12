package com.learn.project.md3_ss12.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class EmployeeCreateDTO {
    private String fullName;
    private String email;
    private String department;
    private MultipartFile avatarFile;
}
