package com.example.authorization.controller;


import com.example.authorization.costant.Costant;
import com.example.authorization.dto.AddToMonitorDto;
import com.example.authorization.dto.HostDto;
import com.example.authorization.dto.PluginsDto;
import com.example.authorization.utils.UtilFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;

@RestController
@RequestMapping("/auth/host")
public class controllerToHostMicroservice {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilFunction utilFunction;

    @Autowired
    private Costant costant ;







    @GetMapping("/getHosts")
    public ResponseEntity<?> getAllHosts(@RequestHeader("Pwd") String password){

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));

        try{

            ResponseEntity<Object> resp = restTemplate.exchange(costant.getGetMonitoredHostUrl(), HttpMethod.GET,
                    body, Object.class);

            return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }




    @GetMapping("/unmonitored/hosts")
    public  ResponseEntity<?> unmonitoredHosts(@RequestHeader("Pwd") String password){
        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));

        System.out.println(utilFunction.getCheckMkAuthHeader(password));
        try{
           ResponseEntity<Object> resp = restTemplate.exchange(costant.getGetUnmonitoredHostUrl(), HttpMethod.GET,
                    body, Object.class);

           return new ResponseEntity<>(resp.getBody(), HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/update")

    public ResponseEntity<?> updateHost(@RequestHeader("Pwd") String password,
                                        @RequestBody HostDto hostDto){
        HttpEntity<?> body = new HttpEntity<>(hostDto, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();
        System.out.println("host dto " + hostDto);

        try{

             restTemplate.exchange(costant.getGetUpdateHostUrl() +hostDto.getHostName(), HttpMethod.PUT,
                    body, Object.class);

            message.put("message", "host correctly updated");

            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


    // da testare cancellazione host
    @DeleteMapping("/delete/{hostname}")

    public ResponseEntity<?> deleteHost(@RequestHeader("Pwd") String password, @PathVariable("hostname") String hostname){
        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();

        try{

            restTemplate.exchange(costant.getGetDeleteHostUrl()+hostname, HttpMethod.DELETE,
                    body, Object.class);

            message.put("message", "host correctly deleted");

            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }




    @PostMapping("/install/agent/{hostname}")
    public ResponseEntity<?> installAgent(@RequestHeader("Pwd") String password, @PathVariable("hostname") String hostname){

        HttpEntity<?> body = new HttpEntity<>(null, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();

        try {
            ResponseEntity<String> risp =restTemplate.exchange(costant.getGetInstallAgentUrl() +hostname, HttpMethod.POST,
                   body, String.class);

            return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        }
    }


    @PostMapping("/install/plugin/{hostname}")
    public ResponseEntity<?> installPlugin(@RequestHeader("Pwd") String password, @PathVariable("hostname") String hostname,
                                           @RequestBody PluginsDto pluginsDto){


        HttpEntity<?> body = new HttpEntity<>(pluginsDto, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();

        if (pluginsDto.getPluginsName() == null || pluginsDto.getPluginsName().length == 0) {
            message.put("error", "no plugins selected");
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        try {
            ResponseEntity<String> risp =restTemplate.exchange(costant.getGetInstallPluginsUrl() +hostname, HttpMethod.POST,
                    body, String.class);

            return new ResponseEntity<>(risp.getBody(), HttpStatus.OK);
        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

    }



    @PostMapping("/add/toMonitoring")
    public ResponseEntity<?> addToMonitor(@RequestHeader("Pwd") String password, @RequestBody AddToMonitorDto[] addToMonitorDto){


        HttpEntity<?> body = new HttpEntity<>(addToMonitorDto, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();

        try {
            if(addToMonitorDto == null || addToMonitorDto.length == 0){

                message.put("error", "no host selected");
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }


            restTemplate.exchange(costant.getGetAddHostToMonitoringUrl(), HttpMethod.POST,
                    body, Object.class);

            message.put("message", "host correctly added");

            return new ResponseEntity<>(message, HttpStatus.OK);


        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/set/SSH")
    public ResponseEntity <?> setSSH(@RequestHeader("Pwd") String password, @RequestBody HostDto hostDto){
        HttpEntity<?> body = new HttpEntity<>(hostDto, utilFunction.getCheckMkAuthHeader(password));
        HashMap<String, String> message = new HashMap<>();

        try {
            if(hostDto.getUsername() == null || hostDto.getUsername().equals("")){

                message.put("error", "no username selected");
                return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
            }


            restTemplate.exchange(costant.getGetSetSSHCredentialsUrl(), HttpMethod.POST,
                    body, Object.class);

            message.put("message", "SSH auth mode correctly updated");

            return new ResponseEntity<>(message, HttpStatus.OK);


        }catch (Exception e){
            message.put("error", e.getMessage());
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
