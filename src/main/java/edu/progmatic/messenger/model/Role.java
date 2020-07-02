package edu.progmatic.messenger.model;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class Role implements GrantedAuthority {
    private String name;
    @Id
    @GeneratedValue
    private Long id;
    @ManyToMany
    Set<User> userSet;
    public Role(){
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<User> getUserSet() {
        return userSet;
    }
    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }
    @Override
    public String getAuthority() {
        return name;
    }
}