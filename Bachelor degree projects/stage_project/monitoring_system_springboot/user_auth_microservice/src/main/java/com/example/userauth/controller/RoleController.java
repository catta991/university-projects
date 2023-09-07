package com.example.userauth.controller;

import com.example.userauth.domain.Role;
import com.example.userauth.dto.MultichoiceReactDto;
import com.example.userauth.userservice.RoleService;
import com.example.userauth.userservice.UserService;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/roles")
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;



    @Bean()
    public void addRoles(){
        roleService.addRolesToDb();
    }



    @GetMapping
    public ResponseEntity<ArrayList<Role>> getRoles() {


        return new ResponseEntity<>((ArrayList<Role>) roleService.getRoles(), HttpStatus.OK);
    }

    @GetMapping(value = "/reactObj")
    public ResponseEntity<ArrayList<MultichoiceReactDto>> getRolesForReact() {
        List<Role> roles = roleService.getRoles();

        ArrayList<MultichoiceReactDto> reactObject = new ArrayList<>();

        for (Role r : roles) {
            if (!r.getName().equals("ROLE_SUPER_ADMIN")) {
                System.out.println(r.getName());
                MultichoiceReactDto obj = new MultichoiceReactDto(r.getName(), r.getName());
                reactObject.add(obj);
            }

        }
        return new ResponseEntity<>(reactObject, HttpStatus.OK);
    }

}
