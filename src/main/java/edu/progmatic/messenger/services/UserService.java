package edu.progmatic.messenger.services;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserDetailsManager userDetailsManager;
    List<User> users = new ArrayList<>();

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

    public void createNewUser(RegistrationDTO registrationDTO) {
        User user = new User(registrationDTO.getUsername(),registrationDTO.getPassword(),registrationDTO.getPasswordConfirm()
                ,registrationDTO.getBirthday(),registrationDTO.getEmail());
        user.addAuthority("ROLE_USER");
        users.add(user);
        userDetailsManager.createUser(user);
    }

    public List<User> findAllUsers(){
        return users.stream().filter(user -> user.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_USER")))
                .collect(Collectors.toList());
    }

    public void promoteToAdmin(String username){
        users.stream().filter(user -> user.getUsername().equals(username)).findFirst()
                .ifPresent(User::removeAuthority);
        users.stream().filter(user -> user.getUsername().equals(username)).findFirst()
                .ifPresent(user -> user.addAuthority("ROLE_ADMIN"));
    }
}
