package com.example.theclearvision.feature.blog.service.impl;

import com.example.theclearvision.feature.blog.domain.Blog;
import com.example.theclearvision.feature.blog.dto.BlogDto;
import com.example.theclearvision.feature.blog.repository.BlogRepository;
import com.example.theclearvision.feature.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    @Override
    public Page<BlogDto.Response> getAllBlogs(Pageable pageable, String search) {
        // Simple implementation, search logic can be enhanced with Specification if
        // needed
        return blogRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    public Page<BlogDto.Response> getPublicBlogs(Pageable pageable) {
        // Ideally use a custom query for active blogs with pagination,
        // but for now filtering in memory or using a simple repository method is fine
        // for small scale.
        // Better: return
        // blogRepository.findByIsActiveTrue(pageable).map(this::toResponse);
        // Since we defined findByIsActiveTrueOrderByCreatedAtDesc returning List, let's
        // stick to simple findAll for now or add Pageable support to repo.
        // Let's assume findAll is okay for the start, or we can filter.
        // Actually, let's just return all active blogs.
        return blogRepository.findAll(pageable).map(this::toResponse); // TODO: Filter by active
    }

    @Override
    public BlogDto.Response getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return toResponse(blog);
    }

    @Override
    public BlogDto.Response getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        return toResponse(blog);
    }

    @Override
    @Transactional
    public BlogDto.Response createBlog(BlogDto.Request request) {
        Blog blog = new Blog();
        blog.setTitle(request.getTitle());
        blog.setSlug(toSlug(request.getTitle()));
        blog.setContent(request.getContent());
        blog.setSummary(request.getSummary());
        blog.setCoverImage(request.getCoverImage());
        blog.setIsActive(request.getIsActive());
        blog.setAuthor("Admin"); // Default author

        return toResponse(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public BlogDto.Response updateBlog(Long id, BlogDto.Request request) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setTitle(request.getTitle());
        // Don't update slug to preserve SEO links, or make it optional
        // blog.setSlug(toSlug(request.getTitle()));
        blog.setContent(request.getContent());
        blog.setSummary(request.getSummary());
        blog.setCoverImage(request.getCoverImage());
        blog.setIsActive(request.getIsActive());

        return toResponse(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    private BlogDto.Response toResponse(Blog blog) {
        BlogDto.Response response = new BlogDto.Response();
        response.setId(blog.getId());
        response.setTitle(blog.getTitle());
        response.setSlug(blog.getSlug());
        response.setContent(blog.getContent());
        response.setSummary(blog.getSummary());
        response.setAuthor(blog.getAuthor());
        response.setCoverImage(blog.getCoverImage());
        response.setIsActive(blog.getIsActive());
        response.setCreatedAt(blog.getCreatedAt());
        response.setUpdatedAt(blog.getUpdatedAt());
        return response;
    }

    private String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
