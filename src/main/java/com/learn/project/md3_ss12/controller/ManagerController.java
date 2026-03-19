package com.learn.project.md3_ss12.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String testAdmin(){
        return "Test thành công với Admin";
    }
    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public String testUser(){
        return "Test thành công với User";
    }
}
