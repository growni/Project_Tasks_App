package com.ipsos.services;

import com.ipsos.entities.Role;

public interface RoleService {
    Role createRole(String roleName);
    Role getByName(String roleName);
}
