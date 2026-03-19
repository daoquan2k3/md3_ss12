package com.learn.project.md3_ss12.security.principle;

import com.learn.project.md3_ss12.entity.Users;
import com.learn.project.md3_ss12.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceCustom implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usename ko tồn tại"));
        List<SimpleGrantedAuthority> list = user.getRoleSet().stream().map(
                role-> new SimpleGrantedAuthority(role.getRoleName().name())
        ).toList();
        return UserDetailCustom.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(list)
                .build();
    }
}
