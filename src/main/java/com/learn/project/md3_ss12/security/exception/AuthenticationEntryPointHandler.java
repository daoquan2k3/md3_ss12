package com.learn.project.md3_ss12.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Code : 401
        // message : un authentication - mô tả rõ ai sai quyền truy cập
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 403

        String ex = (String) request.getAttribute("exception");
        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        data.put("error", "Unauthorized");
        if("ExpiredJwtException".equals(ex)){
            data.put("message", "Token đã hết hạn");
        }else {
            data.put("message", "Truy cập bị từ chối");
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);

        response.getWriter().write(json);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
