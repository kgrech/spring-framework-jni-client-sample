package com.github.kgrech.statcollectior.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


/**
 * Spring security settings
 * @author Konstantin G. (kgrech@mail.ru)
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientsDetailsService clientsDetailsService;

    /**
     * Enables requires basic authentication for any request.
     * If by some reasons basic auth is not safe, it could be replaced
     * with digest auth or https could be used.
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception in case of configuration issue
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and().httpBasic()
                .and().csrf().disable();
    }

    /**
     * Setup user authentication manager builder to validate users credentials
     * @param auth authentication manager builder
     * @throws Exception  in case of configuration issue
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientsDetailsService);
    }
}
