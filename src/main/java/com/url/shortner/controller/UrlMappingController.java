package com.url.shortner.controller;

import com.url.shortner.dtos.UrlMappingDTO;
import com.url.shortner.models.User;
import com.url.shortner.service.UrlMappingService;
import com.url.shortner.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/urls")
@AllArgsConstructor
public class UrlMappingController {

    // Using Logger for better debugging than System.out.println
    private static final Logger LOGGER = Logger.getLogger(UrlMappingController.class.getName());

    private final UrlMappingService urlMappingService;
    private final UserService userService;

    // Correcting the path to be lowercase for consistency with Spring best practices.
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request, Principal principal){
        // The Principal object is only available if the request is successfully authenticated.
        // If principal is null, it means the JWT filter failed to authenticate the user.
        if (principal == null) {
            LOGGER.warning("Authentication failed. Principal is null.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String originalUrl = request.get("originalUrl");
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        LOGGER.info("Authenticated user: " + principal.getName());

        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            LOGGER.severe("User not found in database: " + principal.getName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }
}
