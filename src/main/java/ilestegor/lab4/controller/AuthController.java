package ilestegor.lab4.controller;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import ilestegor.lab4.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JWTRequestDto requestDto, HttpServletResponse response) {
        JwtResponseDto responseDto = authService.login(requestDto, response);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDto> register(@RequestBody JWTRequestDto requestDto, HttpServletResponse response) {
        return new ResponseEntity<>(authService.register(requestDto, response), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return new ResponseEntity<>(authService.logout(response), HttpStatus.OK);
    }
}
