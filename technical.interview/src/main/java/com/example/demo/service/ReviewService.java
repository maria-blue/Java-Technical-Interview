package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Review createReview(Long bookId, Long userId, Review review) {
        User registeredUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User must be registered to leave a review for a book."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        review.setUser(registeredUser);
        review.setBook(book);
        return reviewRepository.save(review);
    }
}