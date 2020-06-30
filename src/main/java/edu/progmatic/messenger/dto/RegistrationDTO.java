package edu.progmatic.messenger.dto;

import edu.progmatic.messenger.constans.DateFormats;
import edu.progmatic.messenger.passwordValidation.ValidPassword;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class RegistrationDTO {

    @Size(min = 3, max = 20)
    @NotNull
    private String username;
    @ValidPassword
    private String password;
    @ValidPassword
    private String passwordConfirm;
    @NotNull
    @DateTimeFormat(pattern = DateFormats.DATE_FORMAT)
    private LocalDate birthday;
    @NotEmpty
    @NotNull
    @Email
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
