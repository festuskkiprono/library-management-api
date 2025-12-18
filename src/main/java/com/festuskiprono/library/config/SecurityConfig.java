package com.festuskiprono.library.config;

import com.festuskiprono.library.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor

public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private  final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    //Performs actual authentication logic
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        //Stateless sessions
        http
                .sessionManagement(c->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .csrf( c->c.disable())
                .authorizeHttpRequests(c -> c
                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers("/auth/refresh", "/auth/validate").permitAll()

                        // Borrow endpoints - authenticated users only
                        .requestMatchers(HttpMethod.PUT,"/borrow/*/return").hasAnyRole( "ADMIN")
                        .requestMatchers("/carts/**").authenticated()

                        // Book management - librarian/admin only
                        .requestMatchers(HttpMethod.POST, "/books").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasAnyRole("LIBRARIAN", "ADMIN")

                        //User management ADMIN Only
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("LIBRARIAN", "ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                                jwtAuthenticationFilter, // Instance of our custom JWT filter
                                UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        c.accessDeniedHandler((request, response, accessDeniedException) ->
                response.setStatus(HttpStatus.FORBIDDEN.value()));
                } );
        //Disable csrf

        //authorize http requests
        return http.build(); //to get a security filter chain object
    }
}
