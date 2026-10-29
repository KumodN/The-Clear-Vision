package com.example.theclearvision.feature.blog.repository;

import com.example.theclearvision.feature.blog.domain.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findBySlug(String slug);

    List<Blog> findByIsActiveTrueOrderByCreatedAtDesc();
}
