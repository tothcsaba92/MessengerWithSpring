package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationUser(@ModelAttribute(value = "newUser") @Valid RegistrationDTO newUser,
                                   BindingResult bindingResult) {

        FieldError fieldError = new FieldError("nameError", "name", "van már ilyen név");
        FieldError fieldError2 = new FieldError("passwordError", "password", "nem egyezik a két jelszó");
        FieldError fieldError3 = new FieldError("passwordError", "passwordConfirm", "nem egyezik a két jelszó");

        bindingResult.addError(fieldError);
        bindingResult.addError(fieldError2);
        bindingResult.addError(fieldError3);

        boolean isNameValid = userService.userNameValidation(newUser.getName());
        boolean isPasswordValid = userService.userPasswordValidation(newUser.getPassword(), newUser.getPasswordConfirm());

        if (bindingResult.hasErrors()) {
            return "registration";
        }


        userService.createUser(newUser.getName(), newUser.getPassword(), "USER");
        return "redirect:/login";
    }

}

