package edu.progmatic.messenger.services;

import edu.progmatic.messenger.dto.RegistrationDTO;
import edu.progmatic.messenger.model.Role;
import edu.progmatic.messenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PersistenceContext
    EntityManager em;


    public boolean userNameValidation(String userName) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.name = :userName", User.class)
                .setParameter("userName", userName).getResultList();
        return !users.isEmpty();
    }

    public boolean emailValidation(String email) {
        List<User> emails = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email).getResultList();
        return !emails.isEmpty();
    }

    public boolean userPasswordValidation(String password, String passwordConfirmation) {
        return passwordConfirmation.equals(password);
    }

    @Transactional
    public void createNewUser(RegistrationDTO registrationDTO) {
        User user = new User(registrationDTO.getUsername(), bCryptPasswordEncoder.encode(registrationDTO.getPassword()),
                bCryptPasswordEncoder.encode(registrationDTO.getPasswordConfirm())
                , registrationDTO.getBirthday(), registrationDTO.getEmail());
        Set<Role> roles = new HashSet<>();
        Role userRole = em.createQuery("SELECT r FROM Role r WHERE r.name  = :roleName", Role.class)
                .setParameter("roleName", ROLE_USER)
                .getSingleResult();
        roles.add(userRole);
        user.setRoles(roles);
        em.persist(user);
    }

    public List<User> findNonAdminUsers() {
        return em.createQuery("SELECT u FROM User u WHERE u NOT IN (SELECT  user FROM User user JOIN user.roles r " +
                " WHERE r.name = 'ROLE_ADMIN') ")
                .getResultList();
    }

    @Transactional
    public void promoteToAdmin(String username) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.name = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        Role adminRole = em.createQuery("SELECT r FROM Role r WHERE r.name = :roleName", Role.class)
                .setParameter("roleName", ROLE_ADMIN)
                .getSingleResult();
        user.getRoles().add(adminRole);
        em.persist(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) em.createQuery("SELECT u FROM User u WHERE u.name = :username")
                .setParameter("username", username)
                .getSingleResult();
    }
}