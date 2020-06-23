package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping(value = "/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String registerUser(Model model) {
        model.addAttribute("newUser", new RegistrationDTO());
        return "registration";
    }
    @PreAuthorize("hasAuthority('GLOBAL_ADMINISTRATOR')")
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationUser(@ModelAttribute(value = "newUser")  RegistrationDTO newUser,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if(userService.userNameValidation(newUser.getName())){
            bindingResult.addError(new FieldError("newUser", "name", "A felhasznalo nev mar foglalt"));
            return "registration";
        } else if(!userService.userPasswordValidation(newUser.getPassword(),newUser.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("newUser", "password", "Nem egyezik a két jelszó"));
            return "registration";
        } else {
            userService.createUser(newUser.getName(),newUser.getPassword(),newUser.getPasswordConfirm(),
                    newUser.getBirthday(),newUser.getEmail());
            return "redirect:/login";
        }

    }

}

