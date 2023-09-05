package com.oauth.implementation.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oauth.implementation.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(String name);
}
