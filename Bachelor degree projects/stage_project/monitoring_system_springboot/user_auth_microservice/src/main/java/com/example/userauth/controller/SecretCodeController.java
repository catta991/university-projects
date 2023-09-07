package com.example.userauth.controller;

import com.example.userauth.domain.SecretCode;
import com.example.userauth.dto.CrudUserDto;
import com.example.userauth.userservice.SecretCodeServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/secretCode")
@AllArgsConstructor

public class SecretCodeController {

    private final SecretCodeServiceImpl secretCodeService;
    private final UserController userController;

    @Bean()
    public void createCode() {

        String secretCode = "forza milan";

        secretCodeService.generateSecretCode(secretCode);
    }


    @GetMapping("/get/entity")
    public ResponseEntity<?> getEntity(){

        try{
            List<SecretCode> secretCodeList= secretCodeService.getSecretCodeEntity();
            if(!secretCodeList.isEmpty())
                return new ResponseEntity<>(secretCodeList.get(0), HttpStatus.OK);
            else
                return new ResponseEntity<>("no secret code entity is present", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }


    // da testare
    @PostMapping("/set/entity")
    public ResponseEntity<?> setEntity(@RequestHeader("Authorization") String auth, @RequestBody CrudUserDto crudUserDto){

        try{
            SecretCode secretCodeEntity = secretCodeService.setEntity();
            if(secretCodeEntity == null)
                return new ResponseEntity<>("the update is impossible", HttpStatus.INTERNAL_SERVER_ERROR);

            userController.updateUser(auth, crudUserDto);

            return new ResponseEntity<>("user correctly updated", HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}
