package com.example.userauth.userservice;

import com.example.userauth.domain.Role;
import com.example.userauth.repo.RoleRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public void addRolesToDb(){
        List<Role> roles = roleRepo.findAll();

        if(roles.isEmpty()){
            saveRoles();
        } else if(roles.size() != 3){
            roleRepo.deleteAll();
            saveRoles();
        }
    }



    private void saveRoles(){
        Role admin = new Role(null, "ROLE_ADMIN", new ArrayList<>());
        Role user = new Role(null, "ROLE_USER", new ArrayList<>());
        Role superAdmin = new Role(null, "ROLE_SUPER_ADMIN", new ArrayList<>());
        roleRepo.save(superAdmin);
        roleRepo.save(admin);
        roleRepo.save(user);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role saveRole(Role role) {
        // log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }


    @Override
    public Role findByName(String roleName) {
        return roleRepo.findByName(roleName);
    }
}

