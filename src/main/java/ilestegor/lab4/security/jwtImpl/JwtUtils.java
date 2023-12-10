package ilestegor.lab4.security.jwtImpl;

import ilestegor.lab4.security.IJwtUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;

@Component
@Slf4j
public class JwtUtils implements IJwtUtils {
    @Value("${jwt.secret}")
    private String tokenSecret;

    @Value("#{T(java.time.Duration).parse('${jwt.lifetime}')}")
    private Duration tokenLifeTime;

    @Override
    public String generateAccessToken(Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", rolesList);
        Date start = new Date();
        Date end = new Date(start.getTime() + tokenLifeTime.toMillis());

        return Jwts.builder().setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(start).setExpiration(end)
                .signWith(SignatureAlgorithm.HS256, tokenSecret).compact();

    }

    @Override
    public String generateAccessToken(String username, Collection<GrantedAuthority> roleEntities) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = roleEntities.stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", rolesList);
        Date start = new Date();
        Date end = new Date(start.getTime() - tokenLifeTime.toMillis());

        return Jwts.builder().setClaims(claims)
                .setSubject(username).setIssuedAt(start)
                .setExpiration(end).signWith(SignatureAlgorithm.HS256, tokenSecret).compact();
    }

    @Override
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
    }

    @Override
    public String getUserNameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token);
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException ex) {
            return false;
        }
    }
}
