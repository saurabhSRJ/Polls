package com.mrwhite.polls.Config;

import com.mrwhite.polls.Filter.AuthenticationExceptionFilter;
import com.mrwhite.polls.Security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationExceptionFilter authenticationExceptionFilter;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }
    //AuthenticationManagerBuilder is used to create an AuthenticationManager. Allows for easily building in memory authentication, LDAP authentication,
    // JDBC based authentication,adding UserDetailsService, and adding AuthenticationProvider's
    //AuthenticationManager Attempts to authenticate the passed {@link Authentication} object, returning a
    //* fully populated <code>Authentication</code> object (including granted authorities) if successful

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //by default Spring Security protects any incoming POST (or PUT/DELETE/PATCH) request with a valid CSRF token
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationExceptionFilter)
                .and().authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }
    //httpBasic will check for Authorization: Basic in secured endpoints to check for valid credentials.
    //This extraction will be automatic.
}
