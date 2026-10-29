package com.example.theclearvision.feature.blog.service;

import com.example.theclearvision.feature.blog.dto.BlogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    Page<BlogDto.Response> getAllBlogs(Pageable pageable, String search);

    Page<BlogDto.Response> getPublicBlogs(Pageable pageable);

    BlogDto.Response getBlogBySlug(String slug);

    BlogDto.Response getBlogById(Long id);

    BlogDto.Response createBlog(BlogDto.Request request);

    BlogDto.Response updateBlog(Long id, BlogDto.Request request);

    void deleteBlog(Long id);
}
