package com.example.theclearvision.feature.blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

public class BlogDto {

    @Data
    public static class Request {
        private String title;
        private String content;
        private String summary;
        private String coverImage;
        private Boolean isActive;
    }

    @Data
    public static class Response {
        private Long id;
        private String title;
        private String slug;
        private String content;
        private String summary;
        private String author;
        private String coverImage;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
