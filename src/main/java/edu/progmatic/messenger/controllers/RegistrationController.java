package edu.progmatic.messenger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {
    @GetMapping(value = "/login_page")
    public String loginUser(){
        return "login_page";
    }
}
