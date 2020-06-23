package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.model.Message;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {

    @GetMapping(value = "/login")
    public String loginUser(){
        return "login";
    }

    @GetMapping(value = "/registration")
    public String registerUser(Model model){
        model.addAttribute("newUser",new RegistrationDTO());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationUser(@ModelAttribute(value ="newUser") @Valid RegistrationDTO newUser,
                                   BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/registration";
        }
        return "redirect:/login";
    }

}

