package com.learn.project.md3_ss12.service;

import com.learn.project.md3_ss12.dto.FormLogin;
import com.learn.project.md3_ss12.dto.FormRegister;
import com.learn.project.md3_ss12.dto.JwtResponse;

public interface IAuthenticationService {
    void register(FormRegister dto);
    JwtResponse login(FormLogin dto);
}
