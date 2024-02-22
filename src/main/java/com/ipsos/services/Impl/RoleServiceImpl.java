package com.ipsos.services.Impl;

import com.ipsos.entities.Role;
import com.ipsos.exceptions.EntityMissingFromDatabase;
import com.ipsos.repositories.RoleRepository;
import com.ipsos.services.RoleService;
import org.springframework.stereotype.Service;

import static com.ipsos.constants.ErrorMessages.RoleOperations.ROLE_NOT_FOUND;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(String roleName) {
        Role role = new Role(roleName);

        return this.roleRepository.save(role);
    }

    @Override
    public Role getByName(String roleName) {
        Role role = this.roleRepository.findByName(roleName);

        if(role == null) {
            throw new EntityMissingFromDatabase(String.format(ROLE_NOT_FOUND, roleName));
        }

        return role;
    }
}
