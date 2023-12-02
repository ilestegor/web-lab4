package ilestegor.lab4.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public interface IJwtUtils {
    String generateAccessToken(Authentication authentication);
    Claims getClaimsFromToken(String token);
    String getUserNameFromToken(String token);
    boolean validateAccessToken(String token);
}
