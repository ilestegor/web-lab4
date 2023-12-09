package ilestegor.lab4.controller;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import ilestegor.lab4.dto.SimpleResponse;
import ilestegor.lab4.exceptions.UserAlreadyExistsException;
import ilestegor.lab4.security.IJwtUtils;
import ilestegor.lab4.service.AuthService;
import ilestegor.lab4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
   private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JWTRequestDto requestDto, HttpServletResponse response){
        log.info(requestDto.username());
        log.info(requestDto.password());
        JwtResponseDto responseDto = authService.login(requestDto, response);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @PostMapping("register")
    public ResponseEntity<JwtResponseDto> register(@RequestBody JWTRequestDto requestDto, HttpServletResponse response){
        return new ResponseEntity<>(authService.register(requestDto, response), HttpStatus.OK);
    }
}
