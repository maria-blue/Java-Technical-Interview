package com.example.demo.repository;

import com.example.demo.model.Review;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    void deleteByIdAndBookId(Long reviewId, Long bookId);
}
