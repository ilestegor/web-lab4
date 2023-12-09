package ilestegor.lab4.service.serviceImpl;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.dto.JwtResponseDto;
import ilestegor.lab4.dto.SimpleResponse;
import ilestegor.lab4.entity.UserEntity;
import ilestegor.lab4.exceptions.UserAlreadyExistsException;
import ilestegor.lab4.security.IJwtUtils;
import ilestegor.lab4.service.AuthService;
import ilestegor.lab4.service.RoleService;
import ilestegor.lab4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final IJwtUtils ijwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, IJwtUtils ijwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.ijwtUtils = ijwtUtils;
    }

    @Override
    public JwtResponseDto login(JWTRequestDto jwtRequestDto, HttpServletResponse response) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequestDto.username(), jwtRequestDto.password()));
        } catch (AuthenticationException ex){
            throw new ilestegor.lab4.exceptions.AuthenticationException("Login or password are incorrect");
        }
        String token = ijwtUtils.generateAccessToken(authentication);
        Cookie cookie = new Cookie("Token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10);
        response.addCookie(cookie);
        return new JwtResponseDto(token, "Bearer");
    }

    @Override
    public JwtResponseDto register(JWTRequestDto jwtRequestDto, HttpServletResponse response) {
        if (userService.isUserExistByName(jwtRequestDto.username())){
            throw new UserAlreadyExistsException("User with this username already exists");
        }
        userService.addUser(jwtRequestDto, "USER");
        return login(jwtRequestDto, response);
    }
}