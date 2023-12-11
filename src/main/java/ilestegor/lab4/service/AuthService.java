package ilestegor.lab4.service;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponseDto login(JWTRequestDto jwtRequestDto, HttpServletResponse response);

    JwtResponseDto register(JWTRequestDto jwtRequestDto, HttpServletResponse response);

    JwtResponseDto logout(HttpServletResponse response);
}
