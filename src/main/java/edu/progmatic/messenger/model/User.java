package edu.progmatic.messenger.model;
import edu.progmatic.messenger.constans.DateFormats;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class User implements UserDetails {

    private String name;
    private String password;
    private String passwordConfirm;
    @DateTimeFormat(pattern = DateFormats.DATE_FORMAT)
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Message> messageList = new ArrayList<>();
    @ManyToMany(mappedBy = "userSet")
    private  Set<Role> roleSet = new HashSet<>();
    @Id
    @GeneratedValue
    private Long id;
    public User(String name, String password, String passwordConfirm, LocalDate birthDate, String email) {
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.birthDate = birthDate;
        this.email = email;
    }
    public User(){
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleSet;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return name;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordConfirm() {
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    public void addRole(Role role){
        roleSet.add(role);
    }

    public String getName() {
        return name;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}