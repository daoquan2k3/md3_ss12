package com.learn.project.md3_ss12.service;

import com.learn.project.md3_ss12.dto.FormLogin;
import com.learn.project.md3_ss12.dto.FormRegister;
import com.learn.project.md3_ss12.dto.JwtResponse;
import com.learn.project.md3_ss12.entity.Role;
import com.learn.project.md3_ss12.entity.RoleName;
import com.learn.project.md3_ss12.entity.Users;
import com.learn.project.md3_ss12.repository.IRoleRepository;
import com.learn.project.md3_ss12.repository.IUserRepository;
import com.learn.project.md3_ss12.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService{
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final IRoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;
    @Value("${jwt.expired}")
    private long expired;
    @Override
    public void register(FormRegister dto) {
        // biến đổi dto -> entity
        Users entity = new Users();
        entity.setEmail(dto.getEmail());
        entity.setFullName(dto.getFullName());
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setPhone(dto.getPhone());

        // biến đoie quyền
        Set<Role> roleSet = new HashSet<>();
        if(dto.getRoles()==null || dto.getRoles().isEmpty()){
            roleSet.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow());
        }else {
            dto.getRoles().forEach(r->{
                switch (r.toLowerCase()){
                    case "admin":
                        roleSet.add(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow());
                        break;
                    case "user":
                        roleSet.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow());
                        break;
                    default:
                        throw new RuntimeException("ko hợp lệ");
                }
            });
        }
        entity.setRoleSet(roleSet);

        userRepository.save(entity);
    }

    @Override
    public JwtResponse login(FormLogin dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto
                            .getEmail(),dto.getPassword())
            );

            String accessToken = jwtProvider.generateAccessToken(dto.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(dto.getEmail());
            // trả về token
            return JwtResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(userRepository.findByEmail(dto.getEmail()).orElseThrow())
                    .expired(new Date(new Date().getTime()+expired))
                    .build();
        }catch (Exception e){
            return null;
        }
    }
}
