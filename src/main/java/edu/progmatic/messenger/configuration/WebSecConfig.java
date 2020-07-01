package edu.progmatic.messenger.configuration;

import edu.progmatic.messenger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        User user = new User("user","password","password",
                LocalDate.of(1992,10,22),"toth@gmail.com");
        user.addAuthority("ROLE_ADMIN");
        manager.createUser(user);
        return manager;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/home")
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .and()
                .authorizeRequests()
                .antMatchers("/messages", "/messages/**", "/home","/exception",
                        "/pics/msg_background.jpeg","/webjars/**", "/registration", "/css/**","/members", "/statics/pics/**","/","404","500")
                .permitAll()
                .anyRequest().authenticated()
                ;
    }


    @SuppressWarnings("deprecation")
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
