package edu.progmatic.messenger.dto;


import edu.progmatic.messenger.constans.DateConfig;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class RegistrationDTO {

    @Size(min = 3, max = 20)
    @NotNull
    private String name;
    @Size(min = 6, max = 20)
    @NotNull
    private String password;
    @Size(min = 6, max = 20)
    @NotNull
    private String passwordConfirm;
    @NotNull
    @DateTimeFormat(pattern = DateConfig.DATE_FORMAT)
    private LocalDate birthday;

    @NotEmpty
    @NotNull
    private String email;

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


}
