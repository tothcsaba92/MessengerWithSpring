package edu.progmatic.messenger.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

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

    public void createUser(String userName, String password, String role) {

        userDetailsManager.createUser(User.withUsername(userName).password(password).roles(role).build());


    }


}
