package edu.progmatic.messenger.services;

import edu.progmatic.messenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    UserDetailsManager userDetailsManager;

    @Autowired
    public UserService(UserDetailsService userDetailsService) {
        this.userDetailsManager = (UserDetailsManager) userDetailsService;
    }

    public boolean userNameValidation(String userName){
         return userDetailsManager.userExists(userName);
    }

    public boolean userPasswordValidation(String password, String passwordConfirmation){
       return passwordConfirmation.equals(password);
    }

    public void createUser(String userName, String password,String passwordConfirm, LocalDate birthday,String email) {
        User user = new User(userName,password,passwordConfirm,birthday,email);
        user.addAuthority("ROLE_USER");
        userDetailsManager.createUser(user);
    }



}
