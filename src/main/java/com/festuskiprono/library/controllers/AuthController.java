package com.festuskiprono.library.controllers;

import com.festuskiprono.library.config.JwtConfig;
import com.festuskiprono.library.dtos.JwtResponse;
import com.festuskiprono.library.dtos.LogInRequest;
import com.festuskiprono.library.dtos.UserDto;
import com.festuskiprono.library.mappers.UserMapper;
import com.festuskiprono.library.repositories.UserRepository;
import com.festuskiprono.library.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
     private final JwtService jwtService;
    private final UserMapper userMapper;
     private final JwtConfig jwtConfig;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> logIn(@Valid @RequestBody LogInRequest request,
                                             HttpServletResponse response)
    {

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        System.out.println(user.getEmail());
        var accessToken= jwtService.generateAccessToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken); //refresh token is sent via a cookie which isn't accessible via javascript making them harder to steal

        cookie.setHttpOnly(true);   // Prevents JavaScript from accessing this cookie - CRITICAL SECURITY FEATURE
        //The cookie can ONLY be accessed by the server (HTTP requests)
        // JavaScript CANNOT read this cookie using document.cookie
        cookie.setPath("/auth/refresh");  // Cookie is ONLY sent to this specific endpoint - Principle of Least Privilege
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());//7d
        cookie.setSecure(true);//cookie to be sent over https token will not be exposed to un encrypted channels

        response.addCookie(cookie);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        return  ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException()
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader("Authorization") String authHeader)
    {
        var token = authHeader.replace("Bearer ", "");
        return  jwtService.validateToken(token);
    }
}

