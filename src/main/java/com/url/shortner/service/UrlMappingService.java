package com.url.shortner.service;

import com.url.shortner.dtos.UrlMappingDTO;
import com.url.shortner.models.URLMapping;
import com.url.shortner.models.User;
import com.url.shortner.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class UrlMappingService {
    private final UrlMappingRepository urlMappingRepository;
    private static final String CHARACTERS = "ABCDEFGHIJLMNOPQRSTUVWXYZabcdefghijlmnopqrstuvwxyz01234567889";
    private static final int SHORT_URL_LENGTH = 8;
    private final Random random = new Random();

    @Transactional
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        // Find existing short URL for the same original URL and user, to prevent duplicates
        Optional<URLMapping> existingMapping = urlMappingRepository.findByOriginalURlAndUser(originalUrl, user);
        if (existingMapping.isPresent()) {
            return convertToDto(existingMapping.get());
        }

        String shortUrl;
        do {
            shortUrl = generateShortUrl();
            // Ensure the generated short URL is unique in the database
        } while (urlMappingRepository.findByShortUrl(shortUrl).isPresent());

        URLMapping urlMapping = new URLMapping();
        urlMapping.setOriginalURl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setClickCount(0);

        URLMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    // New method to retrieve original URL and increment click count
    @Transactional
    public Optional<URLMapping> getOriginalUrlAndIncrementClicks(String shortUrl) {
        Optional<URLMapping> urlMappingOptional = urlMappingRepository.findByShortUrl(shortUrl);

        if (urlMappingOptional.isPresent()) {
            URLMapping urlMapping = urlMappingOptional.get();
            // Increment the click count
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);
        }

        return urlMappingOptional;
    }

    private UrlMappingDTO convertToDto(URLMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalURl());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        urlMappingDTO.setId(urlMapping.getId());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
        return urlMappingDTO;
    }

    private String generateShortUrl() {
        StringBuilder shorturl = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shorturl.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shorturl.toString();
    }
}
