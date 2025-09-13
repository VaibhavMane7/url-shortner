package com.url.shortner.security.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class Jwtutils {
    public String getJwtHeader(HttpServletRequest request){
        String bearerToken= request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
