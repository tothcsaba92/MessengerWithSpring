package edu.progmatic.messenger.controllers;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public UserController(UserService userService) {
        this.userService = userService;
    }

    Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping(value = "/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping(value = "/registration")
    public String viewRegistration(Model model) {
        model.addAttribute("newUser", new RegistrationDTO());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerNewUser(@ModelAttribute(value = "newUser") @Valid RegistrationDTO newUser,
                                  BindingResult bindingResult) {
        logger.info("registration started");
        if (bindingResult.hasErrors()) {
            logger.debug("error found");
            return "registration";
        }
        if (userService.userNameValidation(newUser.getUsername())) {
            bindingResult.addError(new FieldError("newUser", "username", "A felhasznalo nev mar foglalt"));
            logger.debug("error with username validation");
            return "registration";
        } else if (!userService.userPasswordValidation(newUser.getPassword(), newUser.getPasswordConfirm())) {
            bindingResult.addError(new FieldError("newUser", "password", "Nem egyezik a két jelszó"));
            logger.debug("error with password matching");
            return "registration";
        }
        else if(userService.emailValidation(newUser.getEmail())){
           bindingResult.addError(new FieldError("newUser","email","Az email cim mar foglalt"));
           return "registration";
        }  else{
            userService.createNewUser(newUser);
            logger.info("user created");
            return "redirect:/login";
        }

    }

}

