package com.oauth.implementation.controller;

import com.oauth.implementation.model.UserDb;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oauth.implementation.dto.UserRegisteredDTO;
import com.oauth.implementation.service.DefaultUserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private DefaultUserService userService;

    public RegistrationController(DefaultUserService userService) {
        super();
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegisteredDTO userRegistrationDto() {
        return new UserRegisteredDTO();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user")
                                      UserRegisteredDTO registrationDto) {

        // la password deve contenere almeno un numero, una lettera minuscola e
        // una maiuscola un simbolo speciale tra i seguenti: ! @ # & ( ) e deve avere
        // lungezza compresa tra gli 8 e i 20 caratteri
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$");
        Matcher matcher = pattern.matcher(registrationDto.getPassword());
        UserDb user = userService.findUser(registrationDto.getEmail_id());

        if (user == null) {
            if (matcher.find()) {

                userService.save(registrationDto);
                return "redirect:/login";
            } else {

                return "redirect:/registration?pwdError";
            }
        } else {
            return "redirect:/registration?emailError";
        }
    }
}
