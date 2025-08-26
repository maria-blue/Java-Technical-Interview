package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books/{bookId}/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review createReview(@PathVariable Long bookId, @RequestParam Long userId, @RequestBody Review review) {
        return reviewService.createReview(bookId, userId, review);
    }

    @GetMapping
    public List<Review> getReviews(@PathVariable Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
        reviewRepository.deleteByIdAndBookId(reviewId, bookId);
    }
}
