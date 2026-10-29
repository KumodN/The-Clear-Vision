package com.example.theclearvision.feature.blog.controller;

import com.example.theclearvision.feature.blog.dto.BlogDto;
import com.example.theclearvision.feature.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // Public Endpoints
    @GetMapping("/blogs")
    public ResponseEntity<Page<BlogDto.Response>> getPublicBlogs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(blogService.getPublicBlogs(pageable));
    }

    @GetMapping("/blogs/{slug}")
    public ResponseEntity<BlogDto.Response> getBlogBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(blogService.getBlogBySlug(slug));
    }

    // Admin Endpoints
    @GetMapping("/admin/blogs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<BlogDto.Response>> getAllBlogs(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(blogService.getAllBlogs(pageable, search));
    }

    @GetMapping("/admin/blogs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogDto.Response> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PostMapping("/admin/blogs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogDto.Response> createBlog(@RequestBody BlogDto.Request request) {
        return ResponseEntity.ok(blogService.createBlog(request));
    }

    @PutMapping("/admin/blogs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BlogDto.Response> updateBlog(@PathVariable Long id, @RequestBody BlogDto.Request request) {
        return ResponseEntity.ok(blogService.updateBlog(id, request));
    }

    @DeleteMapping("/admin/blogs/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}
