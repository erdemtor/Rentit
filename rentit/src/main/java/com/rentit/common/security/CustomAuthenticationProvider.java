package com.rentit.common.security;

import com.rentit.invoicing.domain.models.Customer;
import com.rentit.invoicing.domain.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by erdem on 24.05.17.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    CustomerRepository customerRepository;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Customer c = customerRepository.findByEmail(username);
        if(c == null || c.getPassword() == null || !c.getPassword().equals(password)){
            throw new BadCredentialsException("Not Authenticated");
        }
        Collection<GrantedAuthority> grantedAuths  =  new ArrayList<>();
        grantedAuths.add(c);
        return new UsernamePasswordAuthenticationToken(c, password, grantedAuths);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
