package com.rentit.common.security;

/**
 * Created by erdem on 8.05.17.
 */
import com.rentit.invoicing.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                .antMatchers("/api/internal/**").authenticated()
                .and().httpBasic()
                .authenticationEntryPoint((req,res,exc) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You don't have anything to see here"));
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("depot").password("depot").roles("DEPOT")
                .and()
                .withUser("main").password("main").roles("MAINTENANCE");
    }
}