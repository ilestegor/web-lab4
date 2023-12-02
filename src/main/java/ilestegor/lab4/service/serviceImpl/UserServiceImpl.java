package ilestegor.lab4.service.serviceImpl;

import ilestegor.lab4.dto.JWTRequestDto;
import ilestegor.lab4.entity.UserEntity;
import ilestegor.lab4.repository.UserRepository;
import ilestegor.lab4.service.RoleService;
import ilestegor.lab4.service.UserService;
import ilestegor.lab4.utils.PasswordUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordUtils passwordUtils;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordUtils passwordUtils) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordUtils = passwordUtils;
    }

    @Override
    public Boolean isUserExistByName(String name) {
        return userRepository.existsByUsername(name);
    }

    @Override
    public Optional<UserEntity> findUserByUserName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s was not found", username)));
        return new User(
                user.getUsername(), user.getPassword(), roleService.mapRolesToSimpleGrantedAuthority(user.getRole())
        );
    }
    @Override
    public UserEntity addUser(JWTRequestDto jwtRequestDto, String userRole) {
        UserEntity user = new UserEntity();
        user.setUsername(jwtRequestDto.username());
        user.setPassword(passwordUtils.hashPassword(jwtRequestDto.password()));
        user.setRole(List.of(roleService.getUserRoleByName(userRole)));
        return userRepository.save(user);
    }
}
