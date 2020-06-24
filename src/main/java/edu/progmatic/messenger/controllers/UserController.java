package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.model.User;
import edu.progmatic.messenger.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
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
    Logger logger = LoggerFactory.getLogger(UserController.class);

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
    public String registrationUser(@ModelAttribute(value = "newUser")  RegistrationDTO newUser,
                                   BindingResult bindingResult) {
        logger.info("registration started");
        if (bindingResult.hasErrors()) {
            logger.debug("error found");
            return "registration";
        }
        if(userService.userNameValidation(newUser.getName())){
            bindingResult.addError(new FieldError("newUser", "name", "A felhasznalo nev mar foglalt"));
            logger.debug("error with username validation");
            return "registration";
        } else if(!userService.userPasswordValidation(newUser.getPassword(),newUser.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("newUser", "password", "Nem egyezik a két jelszó"));
            logger.debug("error with password matching");
            return "registration";
        } else {
            userService.createUser(newUser.getName(),newUser.getPassword(),newUser.getPasswordConfirm(),
                    newUser.getBirthday(),newUser.getEmail());
            logger.info("user created");
//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //TODO Ezzel mit lehet kezdeni?
            return "redirect:/login";
        }

    }

}

