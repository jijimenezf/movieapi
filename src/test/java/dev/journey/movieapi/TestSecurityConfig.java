package dev.journey.movieapi;

import dev.journey.movieapi.auth.config.ApplicationConfig;
import dev.journey.movieapi.auth.config.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
//@Import({SecurityConfig.class, ApplicationConfig.class})
//@ContextConfiguration(classes = {SecurityConfig.class, ApplicationConfig.class})
@TestPropertySource(locations="classpath:test.properties")
class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // @formatter:off
        /*http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/movies/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/movies/**").hasAnyRole("ADMIN", "USER")
                        //.requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasAnyRole("SUPERADMIN")
                        //.requestMatchers(HttpMethod.GET, "/authorities").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                        .anyRequest().denyAll())
                .httpBasic(withDefaults())
                .csrf(CsrfConfigurer::disable);*/
        http //.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/movies/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/auth/**", "/forgotPassword/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(withDefaults())
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
        //.addFilterBefore(authFilterService, UsernamePasswordAuthenticationFilter.class);
        ;
        // @formatter:on

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user").password(passwordEncoder.encode("user")).roles("USER").build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("USER", "ADMIN").build();
        UserDetails superadmin = User.withUsername("superadmin").password(passwordEncoder.encode("superadmin")).roles("USER", "ADMIN").build();
        UserDetails singleUser = User.withUsername("jose.jimenez@imdb.com").password(passwordEncoder.encode("Not4r3al$")).roles("USER").build();

        return new InMemoryUserDetailsManager(user, admin, superadmin, singleUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /*@Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }*/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
