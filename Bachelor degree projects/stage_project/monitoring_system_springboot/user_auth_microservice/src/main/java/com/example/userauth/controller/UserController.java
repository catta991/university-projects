package com.example.userauth.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.example.userauth.domain.Role;
import com.example.userauth.domain.User;
import com.example.userauth.dto.*;
import com.example.userauth.mapper.SingleUserDtoMapper;
import com.example.userauth.JsonParser.UserArrayParser;
import com.example.userauth.JsonParser.UserParser;
import com.example.userauth.userservice.RoleService;
import com.example.userauth.userservice.UserService;
import com.example.userauth.util.CheckMkUsersJson;
import com.example.userauth.util.Costant;
import com.example.userauth.util.UtilFunctions;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RefreshScope
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {


    private final UserService userService;
    private final RoleService roleService;

    private final CheckMkUsersJson checkMkUsersJson;
    private final RestTemplate restTemplate;
    private final UtilFunctions utilFunctions;
    private final Costant costant;

    private final SingleUserDtoMapper singleUserDtoMapper = new SingleUserDtoMapper();
    private final ModelMapper modelMapperSingleUserDto = singleUserDtoMapper.getSingleUserDtopMapper();


    // genera il messaggio segreto all'avvio dell'app che servirà per creare l'utente super admin



    //ok
    @GetMapping("/usersCheckMk")
    @CrossOrigin(value = "http://localhost:3000/")
    public ResponseEntity<?> getUsersCheckMk(@RequestHeader("Authorization") String auth) {


        // setto gli header

        String[] authHeader = auth.split(" ");
        System.out.println("in get users checkmk");
        HttpEntity<?> httpEntity = new HttpEntity<>(null, utilFunctions.getCheckMkAuthHeader(auth));

        try {


            // ottengo un array di utenti da check mk
            ResponseEntity<UserArrayParser> response = restTemplate.exchange(costant.getALL_SAVE_USER_URL(), HttpMethod.GET, httpEntity, UserArrayParser.class);

            // prendendo tutti gli utenti da check mk e avendo lo username dell'utente loggato posso recuperare i contact
            // group dall'array list response e poi faccio le intersezioni


            // ottengo il body della risposta
            List<UserParser> parsed = new ArrayList<>(Arrays.asList(response.getBody().getValue()));

            List<SingleUserDto> dtos = parsed
                    .stream()
                    .map(user -> modelMapperSingleUserDto.map(user, SingleUserDto.class))
                    .collect(Collectors.toList());


            SingleUserDto currentUser = null;

            for (int i = 0; i < dtos.size(); i++) {

                // da cambiare getSec......
                if (dtos.get(i).getUsername().equals(authHeader[1])) {
                    currentUser = dtos.get(i);
                    System.out.println(currentUser);
                    break;
                }

            }


            System.out.println(currentUser.getRoles()[0]);
            return new ResponseEntity<>(dtos, HttpStatus.OK);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            HashMap<String, String> erroMex = new HashMap<>();
            erroMex.put("message", "ops something went wrong");
            return new ResponseEntity<>(erroMex, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @GetMapping("user/{username}")
    public ResponseEntity<?> getSingleUser(@RequestHeader("Authorization") String auth, @PathVariable("username") String username) {

        HashMap<String, String> risp = new HashMap<>();

        System.out.println("in single user");
        System.out.println(username);

        try {
            HttpEntity<?> httpEntity = new HttpEntity<>(null, utilFunctions.getCheckMkAuthHeader(auth));
            ResponseEntity<UserParser> singleUser = restTemplate.exchange(costant.getSINGLE_USER_URL() + username,
                    HttpMethod.GET, httpEntity, UserParser.class);

            SingleUserDto singleUserDto = modelMapperSingleUserDto.map(singleUser.getBody(), SingleUserDto.class);
            return new ResponseEntity<>(singleUserDto, HttpStatus.OK);
        } catch (Exception e) {
            risp.put("error", e.getMessage());
            return new ResponseEntity<>(risp, HttpStatus.BAD_REQUEST);
        }

    }


    
    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestHeader("Authorization") String auth, @RequestBody CrudUserDto crudUserDto) {

        System.out.println("nel controller ");


        if (crudUserDto.getName() == null || crudUserDto.getUsername() == null ||
                crudUserDto.getEmail() == null || crudUserDto.getPassword() == null ||
                crudUserDto.getSurname() == null) {
            return new ResponseEntity<>("all field are required", HttpStatus.BAD_REQUEST);
        } else {
            User userDb = userService.getUser(crudUserDto.getUsername());
            try {
                if (userDb == null) {

                    String fullName = utilFunctions.getFullName(crudUserDto.getName(), crudUserDto.getSurname());
                    Role role = roleService.findByName(crudUserDto.getRoleName());


                    HttpHeaders headers = utilFunctions.getCheckMkAuthHeader(auth);

                    ResponseEntity<String> req = new ResponseEntity<>(checkMkUsersJson.saveUserCheckMk(fullName, crudUserDto, true),
                            headers, HttpStatus.OK);

                    String personResultAsJsonStr =
                            restTemplate.postForObject(costant.getALL_SAVE_USER_URL(), req, String.class);

                    ResponseEntity<String> reqApp = new ResponseEntity<>(checkMkUsersJson.applyChangesCheckMk(), headers, HttpStatus.OK);


                    String ApplyChangeResultAsJsonStr =
                            restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), reqApp, String.class);


                    User user = new User(fullName, crudUserDto.getUsername(), crudUserDto.getEmail(),
                            crudUserDto.getPassword(), role);


                    return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);

                } else {
                    return new ResponseEntity<>("username already used", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }




    //ok
    @PostMapping("/users/delete")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String auth, @RequestBody UserDto userDto) {
        HashMap<String, String> map = new HashMap<>();
        System.out.println(userDto);
        User user = userService.getUser(userDto.getUsername());

        //System.out.println(user);

        if (user == null) {

            return new ResponseEntity<>("this user doesn't exist", HttpStatus.BAD_REQUEST);
        } else {
            try {

                String deleteUrl = costant.getDELETE_MODIFY_USER_URL() + userDto.getUsername();

                HttpHeaders headers = utilFunctions.getCheckMkAuthHeader(auth);

                HttpEntity<?> httpEntity = new HttpEntity<>(null, headers);

                restTemplate.exchange(deleteUrl, HttpMethod.DELETE, httpEntity, String.class);


                ResponseEntity<String> reqApp = new ResponseEntity<>(checkMkUsersJson.applyChangesCheckMk(), headers, HttpStatus.OK);
                String ApplyChangeResultAsJsonStr =
                        restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), reqApp, String.class);


                userService.deleteUser(user);

                map.put("message", "user correctly deleted");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                map.put("message", "server error please retry");
                return new ResponseEntity<Map<String, String>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

   
    //ok
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Auth");

        System.out.println(authorizationHeader);
        System.out.println("in refresh token");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                // verifico il refresh token
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();

                // ottengo l'utente dal db
                User user = userService.getUser(username);

                if (user == null) {
                    throw new RuntimeException("user not found");
                } else {
                    // creo un nuovo token
                    String access_token = JWT.create()
                            .withSubject(user.getUsername())
                            // così facendo il token dura 10 minuti
                            // da provare se non metto la scadenza se il token non scade o cosa accade
                            .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 1000))
                            .withIssuer(request.getRequestURL().toString())
                            .withClaim("roles", user.getRoleCollection().stream().map(Role::getName).collect(Collectors.toList()))
                            .sign(algorithm);
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", access_token);
                    tokens.put("refresh_token", refresh_token);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                }
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    // ok
    @PutMapping("/update/user")
    ResponseEntity<?> updateUser(@RequestHeader("Authorization") String auth, @RequestBody CrudUserDto crudUserDto) {

        System.out.println("in update user");
        String fullName = utilFunctions.getFullName(crudUserDto.getName(), crudUserDto.getSurname());
        // System.out.println(checkMkUsersJson.saveUserCheckMk(fullName, crudUserDto, false));
        String authParts[] = auth.split(" ");

        HttpHeaders headers = utilFunctions.getCheckMkAuthHeader(auth);


        String modifyUrl = costant.getDELETE_MODIFY_USER_URL() + crudUserDto.getUsername();


        ResponseEntity<String> update = new ResponseEntity<>(checkMkUsersJson.saveUserCheckMk(fullName, crudUserDto, false),
                headers, HttpStatus.OK);

        try {


            Role role = roleService.findByName(crudUserDto.getRoleName());
            User user = userService.getUser(crudUserDto.getUsername());

            if (role == null)
                return new ResponseEntity<>("role not found", HttpStatus.BAD_REQUEST);
            else if (user == null)
                return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);

            restTemplate.exchange(modifyUrl, HttpMethod.PUT, update, String.class);
            ResponseEntity<String> reqApp = null;

            if (crudUserDto.getPassword() != null && !crudUserDto.getPassword().equals("")
            && crudUserDto.getUsername().equals(authParts[1])) {
                System.out.println(utilFunctions.getCheckMkAuthHeaderNewPassword(auth, crudUserDto.getPassword()));

                reqApp = new ResponseEntity<>(checkMkUsersJson.applyChangesCheckMk(),
                        utilFunctions.getCheckMkAuthHeaderNewPassword(auth, crudUserDto.getPassword()),
                        HttpStatus.OK);

            }else {

                reqApp = new ResponseEntity<>(checkMkUsersJson.applyChangesCheckMk(), headers, HttpStatus.OK);
            }

            // se modifica la password prima di applicare i cambiamenti devo cambiare la password nel header

            String ApplyChangeResultAsJsonStr =
                    restTemplate.postForObject(costant.getAPPLY_CHANGES_URL(), reqApp, String.class);

            user.setName(crudUserDto.getName() + " " + crudUserDto.getSurname());
            user.setEmail(crudUserDto.getEmail());
            user.setRole(role);

            System.out.println(user.getPassword());

            if (crudUserDto.getPassword() != null && !crudUserDto.getPassword().equals("")) {
                System.out.println("in password non vuota");
                user.setPassword(crudUserDto.getPassword());

                userService.updateUser(user);
            } else {
                System.out.println("in password vuota");
                userService.updateUserNoPsw(user);
            }

            //JSONObject risp = new JSONObject();

            // risp.put("message", "user correctly modified");

            HashMap<String, String> risp = new HashMap<>();
            risp.put("message", "user correctly updated");

            return new ResponseEntity<>(risp, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("ops something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}


