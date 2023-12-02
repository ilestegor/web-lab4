package ilestegor.lab4.service.serviceImpl;

import ilestegor.lab4.entity.RoleEntity;
import ilestegor.lab4.repository.RoleRepository;
import ilestegor.lab4.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<SimpleGrantedAuthority> mapRolesToSimpleGrantedAuthority(Collection<RoleEntity> roleCollection) {
        return roleCollection.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public RoleEntity getUserRoleByName(String userRoleName) {
        return repository.findByName(userRoleName).orElse(null);
    }
}
