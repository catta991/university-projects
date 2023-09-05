package com.oauth.implementation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * controller per renderizzare la pagina controller.html
 */
@Controller
@RequestMapping("/error")
public class ErrorController {


    @GetMapping
    public String errorPage(Model model) {

        return "error";
    }
}
