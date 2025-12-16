package com.festuskiprono.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        //Stateless sessions
        http
                .sessionManagement(c->
                c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .csrf( c->c.disable())
                .authorizeHttpRequests(c->
                        c.anyRequest().permitAll()
        );
        //Disable csrf

        //authorize http requests
        return http.build(); //to get a security filter chain object
    }
}
