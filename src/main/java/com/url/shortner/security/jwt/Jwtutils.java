package com.url.shortner.security.jwt;

import com.url.shortner.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

import java.util.stream.Collectors;

public class Jwtutils {
    public String getJwtHeader(HttpServletRequest request){
        String bearerToken= request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(UserDetailsImpl userDetails){
        String username=userDetails.getUsername();
        String roles=userDetails.getAuthorities().stream().map(authority->authority.getAuthority()).collect(Collectors.joining(","));
        return Jwts.builder().subject(username).claim("roles",roles)
    }
}
