package com.oauth.implementation.controller;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.oauth.implementation.dto.TokenDTO;
import com.oauth.implementation.dto.UserLoginDTO;
import com.oauth.implementation.util.KeyCloackJsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;


@Controller
public class LoginKeycloackController {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TokenDTO tokenDTO;


    private String userRole;

    private String userName;


    // recupero le informazioni dal form e le salvo nell'oggetto UserLoginDto
    @ModelAttribute("user")
    public UserLoginDTO userLoginDTO() {
        return new UserLoginDTO();
    }

    @GetMapping("/loginKeycloack")
    public String loginKeycloack() {

        return "loginKeycloack";
    }

    @PostMapping("/loginKeycloackCheck")
    public String loginKeycloackCheck(@ModelAttribute("user") UserLoginDTO userLoginDTO) {


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "login-app");
        map.add("username", userLoginDTO.getUsername());
        map.add("password", userLoginDTO.getPassword());
        map.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestBodyFormUrlEncoded = new HttpEntity<>(map, headers);

        try {
            HttpEntity<KeyCloackJsonParser> response = restTemplate.postForEntity("Keycloack url for getting the JWT token",

                    requestBodyFormUrlEncoded, KeyCloackJsonParser.class);

            // se le credenziali non sono valide va direttamente nel catch

            System.out.println("risposta " + response.getBody().getAccess_token());

            boolean verified = verifyJwtTokenKeycloack(response.getBody().getAccess_token());

            if (verified) {

                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

                if (userRole.equals("USER")) {
                    GrantedAuthority grantedAuthority = new GrantedAuthority() {
                        public String getAuthority() {
                            return "USER";
                        }
                    };
                    grantedAuthorities.add(grantedAuthority);
                }


                // setto l'utente loggato all'interno del sistema
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(userName, null, grantedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(auth);


                ArrayList<String> tokens = new ArrayList<>();
                tokens = tokenDTO.generateJWTtokenKeycloack(userName, "http://localhost:8081/login", grantedAuthorities);

                tokenDTO.setAccessToken(tokens.get(0));
                tokenDTO.setRefreshToken(tokens.get(1));


                return "redirect:/dashboardDue";
            } else return "redirect:/loginKeycloack?connError";
        } catch (Exception e) {
            System.out.println(e.getMessage());

            if (e.getMessage().equals("401 Unauthorized: [no body]"))
                return "redirect:/loginKeycloack?error";
            else
                return "redirect:/loginKeycloack?connError";
        }
    }

    private boolean verifyJwtTokenKeycloack(String token) throws CertificateException, MalformedURLException, JwkException {


        DecodedJWT jwt = JWT.decode(token);
        RSAPublicKey publicKey = loadPublicKey(jwt);
        Algorithm algorithm = Algorithm.RSA256(publicKey, null);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            userName = String.valueOf(decodedJWT.getClaim("preferred_username")).replace("\"", "");

            Claim roles = decodedJWT.getClaim("realm_access");
            JSONObject jsonObj = new JSONObject(roles.toString());
            JSONArray ja_data = jsonObj.getJSONArray("roles");

            for (int i = 0; i < ja_data.length(); i++) {
                if (ja_data.getString(i).equals("user"))
                    userRole = "USER";


            }


            return true;
        } catch (Exception e) {
            return false;
        }


    }

    private RSAPublicKey loadPublicKey(DecodedJWT token) throws MalformedURLException, JwkException {

        final String url = getKeycloakCertificateUrl(token);
        JwkProvider provider = new UrlJwkProvider(new URL(url));

        return (RSAPublicKey) provider.get(token.getKeyId()).getPublicKey();
    }

    private String getKeycloakCertificateUrl(DecodedJWT token) {
        return token.getIssuer() + "/protocol/openid-connect/certs";
    }


}


