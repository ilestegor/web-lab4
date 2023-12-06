package ilestegor.lab4.controller;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import ilestegor.lab4.security.IJwtUtils;
import ilestegor.lab4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final IJwtUtils ijwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, IJwtUtils ijwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.ijwtUtils = ijwtUtils;
    }

    @PostMapping("login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody JWTRequestDto requestDto, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.username(), requestDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = ijwtUtils.generateAccessToken(authentication);
        Cookie cookie = new Cookie("Token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return new ResponseEntity<>(new JwtResponseDto(token, "Bearer"), HttpStatus.OK);
    }
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody JWTRequestDto requestDto){
        if (userService.isUserExistByName(requestDto.username())){
            return new ResponseEntity<>(String.format("Username %s is taken", requestDto.username()), HttpStatus.BAD_REQUEST);
        }
        userService.addUser(requestDto, "USER");
        return new ResponseEntity<>(String.format("User %s registered!", requestDto.username()), HttpStatus.OK);
    }
}
