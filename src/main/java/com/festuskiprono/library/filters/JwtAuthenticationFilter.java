package com.festuskiprono.library.filters;

import com.festuskiprono.library.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //extract authorization header from the request
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;

        }
        //if auth header is present then extract token and validate it

        var token = authHeader.replace("Bearer ", "");
        if(!jwtService.validateToken(token))
        {
            System.out.println("Invalid token");
            filterChain.doFilter(request,response);
            return;
        }
        //at this point we have a valid token
        //we create an authentication object to allow user access protected resources

        var authentication = new UsernamePasswordAuthenticationToken(jwtService.getEmailFromToken(token),null,null);
        authentication.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);//store info about currently authenticated user
        //pass control to the next filter
        filterChain.doFilter(request, response);
        //to make sure we have this filter as the first filter:SecurityConfig add the filter before the built-in filter
    }
}
