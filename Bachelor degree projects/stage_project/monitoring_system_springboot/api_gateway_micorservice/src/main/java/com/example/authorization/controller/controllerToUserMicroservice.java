package com.example.authorization.controller;


import com.example.authorization.costant.Costant;
import com.example.authorization.dto.CrudUserDto;
import com.example.authorization.dto.UserDto;
import com.example.authorization.dto.UserLogin;
import com.example.authorization.utils.UtilFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("/auth/user")
public class controllerToUserMicroservice {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilFunction utilFunction;

    @Autowired
    private Costant costant;


    @GetMapping("/saluta")
    public void saluta(){

        System.out.println(costant.getAllContactGroupUrl());
    }

    //ok
    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLogin userLogin) {



    try {
        HttpEntity body = new HttpEntity<>(userLogin);
        ResponseEntity<Object> risp = restTemplate.postForEntity(costant.getLoginUrl(), body, Object.class);

        return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
    }catch (Exception e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }

    //ok
    @GetMapping("/checkMk")
    public ResponseEntity<?> getUsersCheckMk(@RequestHeader("Pwd") String password) {

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));

        System.out.println(body);

        try {
            ResponseEntity<Object> risp = restTemplate.exchange(costant.getCheckMkUserUrl(), HttpMethod.GET,
                    body, Object.class);
            System.out.println(risp);
            return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getSingleUser/{username}")
    public ResponseEntity<?> getSingleUser(@RequestHeader("Pwd") String password, @PathVariable("username") String username){

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));
        System.out.println("get single user");

        try{
            System.out.println(costant.getSingleUserInformationUrl());
            ResponseEntity<Object> resp = restTemplate.exchange(costant.getSingleUserInformationUrl()+username, HttpMethod.GET, body, Object.class);
            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



//ok
    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestHeader("Pwd") String password, @RequestBody CrudUserDto crudUserDto) {
        HttpEntity<?> body = new HttpEntity<>(crudUserDto, utilFunction.getCheckMkAuthHeader(password));

        try {
            ResponseEntity<Object> risp = restTemplate.exchange(costant.getSaveNewUserUrl(), HttpMethod.POST,
                    body, Object.class);
            return new ResponseEntity<>(risp.getBody(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //ok
    @PostMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Pwd") String password, @RequestBody UserDto userDto) {

        HashMap<String, String> respMex = new HashMap<>();
        HttpEntity<?> body = new HttpEntity<>(userDto, utilFunction.getCheckMkAuthHeader(password));
        try {
            ResponseEntity<Object> risp = restTemplate.exchange(costant.getDeleteUserUrl(), HttpMethod.POST,
                    body, Object.class);
            return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            respMex.put("error", e.getMessage());
            return new ResponseEntity<>(respMex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //ok
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Pwd") String password, @RequestBody CrudUserDto crudUserDto) {
        HttpEntity<?> body = new HttpEntity<>(crudUserDto, utilFunction.getCheckMkAuthHeader(password));
        try {
            ResponseEntity<Object> risp = restTemplate.exchange(costant.getUpdateUserUrl(), HttpMethod.PUT,
                    body, Object.class);
            return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//ok
    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("Auth") String refreshToken) {

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getAuthHeader(refreshToken));

        try {

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getRefreshTokenUrl(), HttpMethod.GET, body, Object.class);

            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // ok
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(){

        try {
            ResponseEntity<Object> resp = restTemplate.exchange(costant.getGetRolesUrl(), HttpMethod.GET, null, Object.class);
            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get/secret/code")
    public ResponseEntity<?> getSecretCode(){

        try{
            ResponseEntity<Object> resp = restTemplate.exchange(costant.getGetSecretCodeUrl(),
                    HttpMethod.GET, null, Object.class);


            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/set/secret/code")
    public ResponseEntity<?> setSecretCode(@RequestHeader("Pwd") String password, @RequestBody CrudUserDto crudUserDto){
        HashMap<String, String> respMex = new HashMap<>();
        try {
            HttpEntity<?> body = new HttpEntity<>(crudUserDto, utilFunction.getCheckMkAuthHeader(password));
            ResponseEntity<String> resp = restTemplate.exchange(costant.getSetSecretCodeUrl(), HttpMethod.POST,
                    body, String.class);
            respMex.put("message", resp.getBody());
            return new ResponseEntity<>(respMex, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
