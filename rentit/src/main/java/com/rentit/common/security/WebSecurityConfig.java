package com.rentit.common.security;

/**
 * Created by erdem on 8.05.17.
 */
import com.rentit.invoicing.domain.models.Customer;
import com.rentit.invoicing.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableGlobalMethodSecurity(securedEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/sales/**").authenticated()
                .antMatchers("/api/internal/**").authenticated()
                .antMatchers("/api/maintenance").permitAll()
                .and().httpBasic()
                .authenticationEntryPoint((req,res,exc) ->
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You don't have anything to see here"));
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {

        builder.authenticationProvider(authProvider)
                .inMemoryAuthentication()
                .withUser("erdem").password("erdem").roles("EMPLOYEE");

    }
}