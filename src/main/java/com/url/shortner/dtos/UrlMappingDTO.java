package com.url.shortner.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlMappingDTO {

    private String originalUrl;;
    private String shortUrl;
    private Long id;
    private String username;
    private LocalDateTime createdDate;
    private int clickCount;


}
