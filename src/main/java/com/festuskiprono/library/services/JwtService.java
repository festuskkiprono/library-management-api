package com.festuskiprono.library.services;

import com.festuskiprono.library.config.JwtConfig;
import com.festuskiprono.library.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
//Service that generates JSON web token
@RequiredArgsConstructor
@Service
public class JwtService {
    @Value("${spring.jwt.secret}")
    private String secret;
    private final JwtConfig jwtConfig;




    public String generateAccessToken(User user)
    {

        final long tokenExpiration=300; //5 min
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public String generateToken(User user, long tokenExpiration) {
        //subject ==> anything that uniquely identifies the user
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .compact();//compact generates the token  like build() method of the builder object
    }
    public String generateRefreshToken(User user)
    {

        return generateToken(user, jwtConfig.getRefreshTokenExpiration());

    }
    public boolean validateToken(String token)
    {
        try
        {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());  //If the expiration is after the current date  then the token is valid
        }
        catch(JwtException ex)
        {
            return false;

        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String getEmailFromToken(String token)
    {
        return getClaims(token).getSubject();
    }
    public Long getUserIdFromToken(String token)
    {
        return Long.valueOf(getClaims(token).getSubject());
    }

    /*public Role getUserRoleFromToken(String token)
    {
        return Role.valueOf(getClaims(token).get("role", String.class));
    }*/
}




