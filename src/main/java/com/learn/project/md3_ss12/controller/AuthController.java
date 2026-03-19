package com.learn.project.md3_ss12.controller;

import com.learn.project.md3_ss12.dto.FormLogin;
import com.learn.project.md3_ss12.dto.FormRegister;
import com.learn.project.md3_ss12.dto.JwtResponse;
import com.learn.project.md3_ss12.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
//@CrossOrigin("*")
public class AuthController {
    @Autowired
    private IAuthenticationService authenticationService;
    // dăng kí
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody FormRegister dto){
        // mật khẩu phải mã hóa
//        passwordEncoder.encode() //mã hóa mật khẩu
        authenticationService.register(dto);
        return new ResponseEntity<>("Đăng kí thành công", HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FormLogin dto){
        // mật khẩu phải mã hóa
        JwtResponse r = authenticationService.login(dto);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken){
        // mật khẩu phải mã hóa
//        JwtResponse r = authenticationService.login(dto);
        return new ResponseEntity<>("Tạo token thành công", HttpStatus.OK);
    }
}
