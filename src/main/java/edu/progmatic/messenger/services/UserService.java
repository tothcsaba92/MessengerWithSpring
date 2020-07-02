package edu.progmatic.messenger.services;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.model.Role;
import edu.progmatic.messenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PersistenceContext
    EntityManager em;


    public boolean userNameValidation(String userName) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.name = :userName", User.class)
                .setParameter("userName", userName).getResultList();
        if (!users.isEmpty()) {
            return users.get(0) == null;
        }
        return false;
        //TODO validaciokat megnezni
    }

    public boolean userPasswordValidation(String password, String passwordConfirmation) {
        return passwordConfirmation.equals(password);
    }

    @Transactional
    public void createNewUser(RegistrationDTO registrationDTO) {
        User user = new User(registrationDTO.getUsername(), bCryptPasswordEncoder.encode(registrationDTO.getPassword()),
                bCryptPasswordEncoder.encode(registrationDTO.getPasswordConfirm())
                , registrationDTO.getBirthday(), registrationDTO.getEmail());
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        em.persist(user);
    }

    public List<User> findAllUsers() {
        return em.createQuery("SELECT u FROM User u")
                .getResultList();
    }

    @Transactional
    public void promoteToAdmin(String username) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.name = :username", User.class)
                .setParameter("username", username)
                .getResultList().get(0);
        Role role = new Role();
        role.setName("Admin");
       // user.addRole(role);
        em.persist(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return (UserDetails) em.createQuery("SELECT u FROM User u WHERE u.name = :s")
                .setParameter("s", s)
                .getResultList().get(0);
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}