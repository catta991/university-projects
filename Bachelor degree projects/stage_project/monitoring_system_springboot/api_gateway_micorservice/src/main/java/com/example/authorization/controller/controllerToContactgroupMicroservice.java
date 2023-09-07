package com.example.authorization.controller;

import com.example.authorization.costant.Costant;
import com.example.authorization.dto.ContactGroupDto;
import com.example.authorization.utils.UtilFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class controllerToContactgroupMicroservice {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilFunction utilFunction;

    @Autowired
   private  Costant costant ;


   

    // ok con super admin
    // ok con admin

    @GetMapping("/contactGroups")
    public ResponseEntity<?> getContactGroups(@RequestHeader("Pwd") String password) {

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));

        try {

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getAllContactGroupUrl(),
                    HttpMethod.GET, body, Object.class);

            System.out.println(resp);

            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    //ok
    @PostMapping("/delete/contactgroup")
    public ResponseEntity<?> deleteContactGroup(@RequestHeader("Pwd") String password, @RequestBody ContactGroupDto contactGroupNameDto) {

        System.out.println(contactGroupNameDto);

        try {

            System.out.println(costant.getDeleteUrl()+contactGroupNameDto.getName());

            HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getDeleteUrl()+contactGroupNameDto.getName(), HttpMethod.DELETE, body,
                    Object.class);

            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);
        } catch (Exception e) {
            HashMap<String, String> err = new HashMap<>();
            err.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

    }

    // ok
    @PutMapping("/modify/contactgroup")

    public  ResponseEntity<?> modifyContactGroup(@RequestHeader("Pwd") String password, @RequestBody ContactGroupDto contactGroupDto)
    {

        HashMap<String, String> respMex = new HashMap<>();

        try{

            HttpEntity<?> reqBody=  new HttpEntity<>(contactGroupDto, utilFunction.getCheckMkAuthHeader(password));

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getModifyContactGroupUrl(), HttpMethod.PUT, reqBody, Object.class);

            respMex.put("message", "contact group correctly updated");

            return new ResponseEntity<>(respMex, HttpStatus.OK);
        }catch (Exception e){

            respMex.put("error", e.getMessage());
            return new ResponseEntity<>(respMex, HttpStatus.BAD_REQUEST);
        }
    }



    // ok per superadmin
    // ok per admin
    @PostMapping("/create/contactgroup")
    public ResponseEntity<?> createContactGroup(@RequestHeader("Pwd") String password, @RequestBody ContactGroupDto contactGroupDto)
    {
        HashMap<String, String> respMex = new HashMap<>();

        System.out.println(contactGroupDto);

        try{



            HttpEntity<?> reqBody=  new HttpEntity<>(contactGroupDto, utilFunction.getCheckMkAuthHeader(password));

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getCreateContactGroupUrl(), HttpMethod.POST, reqBody, Object.class);

            respMex.put("message", "contact group correctly created");

            return new ResponseEntity<>(respMex, HttpStatus.OK);
        }catch (Exception e){

            respMex.put("error", e.getMessage());
            return new ResponseEntity<>(respMex, HttpStatus.BAD_REQUEST);
        }
    }


}
