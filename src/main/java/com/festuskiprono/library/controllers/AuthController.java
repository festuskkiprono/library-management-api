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

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh( @CookieValue(value = "refreshToken") String refreshToken)
    {
        //Goal: To refresh access token

        //Receive a cookie named refreshToken which we had created  earlier on

        //validate the cookie
        if(!jwtService.validateToken(refreshToken))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        }
        var userId=jwtService.getUserIdFromToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow();
        var accessToken= jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(accessToken));//Return the refreshed/ new acces token in the body
        //Use epochconverter.com to check expiation of token

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me()  // Endpoint to get currently authenticated user's profile
    {
        //From JwtAuthenticationFilter

        // WHY ACCESS CURRENT USER?
        // 1. Display user profile: Show name, email, avatar in UI (navbar, profile page)
        // 2. Personalization: Customize dashboard, recommendations, settings based on who's logged in
        // 3. Authorization checks: Verify user has permission to access/modify resources
        // 4. Audit logging: Track which user performed actions (orders, posts, updates)
        // 5. User-specific data: Filter content to show only what belongs to this user
        // 6. Session validation: Confirm token is valid and user still exists in system

        // Extracting the current principal (user ID) from the security context
        // SecurityContextHolder stores authentication info set by JwtAuthenticationFilter

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //We are sending the following From JwtAuthenticationFilter
        //SecurityContextHolder.getContext().setAuthentication(authentication);

        var userId = (Long)authentication.getPrincipal(); // Principal contains userId we set in JWT filter

        // Lookup the user in database to get full user details
        // Token only contains userId - we need to fetch complete profile (name, email, etc.)
        var user = userRepository.findById(userId).orElse(null);
        if(user==null)  // User might be deleted after token was issued
        {
            return ResponseEntity.notFound().build(); // 404 - Token valid but user no longer exists
        }

        // Return result as DTO to avoid exposing sensitive fields (password hash, internal IDs)
        var userDto = userMapper.toDto(user);
        System.out.println(userDto); // Debug logging (remove in production)
        return ResponseEntity.ok(userDto); // 200 OK with user profile data
    }
}

