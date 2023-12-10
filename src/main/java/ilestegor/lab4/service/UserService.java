package ilestegor.lab4.service;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.entity.UserEntity;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

@NoRepositoryBean
public interface UserService extends UserDetailsService {
    UserEntity addUser(JWTRequestDto jwtRequestDto, String userRole);

    Boolean isUserExistByName(String name);

    Optional<UserEntity> findUserByUserName(String name);

}
