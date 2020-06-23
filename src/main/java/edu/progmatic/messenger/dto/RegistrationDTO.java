package edu.progmatic.messenger.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationDTO {
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    @Size(min = 6, max = 10)
    private String password;

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 10)
    private String passwordConfirm;

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
