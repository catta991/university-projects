package com.example.resourceservercustom.controller;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    @GetMapping("/user/resources")
    public ResponseEntity<?> getUserResources() {

        Map<String, String> mex = new HashMap<>();
        mex.put("message", "queste sono le risorse degli utenti con ruolo USER");
        return new ResponseEntity<>(mex, HttpStatus.OK);
    }

    @GetMapping("/admin/resources")
    public ResponseEntity<?> getAdminResources() {

        Map<String, String> mex = new HashMap<>();

        mex.put("message", "queste sono le risorse degli utenti con ruolo ADMIN");

        return new ResponseEntity<>(mex, HttpStatus.OK);
    }


}
