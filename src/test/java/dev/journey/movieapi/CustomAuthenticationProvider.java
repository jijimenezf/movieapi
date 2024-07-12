package dev.journey.movieapi;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    UserDetailsService userDetailsService;

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (!checkCustomAuthenticationSystem(username, password)) {
            throw new BadCredentialsException("Bad credentials provided");
        }

        return new UsernamePasswordAuthenticationToken(
                username, password, AuthorityUtils.createAuthorityList("USER"));
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // Use custom authentication system for the verification of the
    // passed username and password.  (Here we are just faking it.)
    private boolean checkCustomAuthenticationSystem(String username, String password) {
        return username.equals("user") && password.equals("user");
    }
}
