package com.example.userauth.userservice;

import com.example.userauth.domain.Role;
import com.example.userauth.domain.User;

import java.util.List;

public interface RoleService {

    List<Role> getRoles();

    Role saveRole(Role role);

    Role findByName(String roleName);

    void addRolesToDb();
}
