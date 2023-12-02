package ilestegor.lab4.service;

import ilestegor.lab4.entity.RoleEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface RoleService {
    Collection<SimpleGrantedAuthority> mapRolesToSimpleGrantedAuthority(Collection<RoleEntity> roleCollection);
    RoleEntity getUserRoleByName(String userRoleName);
}
