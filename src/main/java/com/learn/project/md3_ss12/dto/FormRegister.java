package com.learn.project.md3_ss12.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormRegister {
    private String email;
    private String phone;
    private String fullName;
    private String password;
    private List<String> roles;
}
