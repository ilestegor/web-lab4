package ilestegor.lab4.service;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import ilestegor.lab4.dto.SimpleResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    JwtResponseDto login(JWTRequestDto jwtRequestDto, HttpServletResponse response);
    JwtResponseDto register(JWTRequestDto jwtRequestDto, HttpServletResponse response);
}
