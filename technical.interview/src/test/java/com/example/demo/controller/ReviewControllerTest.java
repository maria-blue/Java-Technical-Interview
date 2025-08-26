package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.Review;
import com.example.demo.model.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReviewService reviewService;

    private Review review;
    private Long bookId;
    private Long userId;

    @BeforeEach
    void setUp() {
        User user = new User(1L, "Maria", "Gal", "mg@gmail.com", LocalDateTime.now());
        Book book = new Book(1L, "The Hobbit", "J.R.R. Tolkien", "ISBN1", 30.0);
        userId = user.getId();
        bookId = book.getId();

        review = new Review();
        review.setId(1L);
        review.setContent("Captivating!");
        review.setRating(5);
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);
        review.setBook(book);
    }

    @Test
    void testCreateReview() throws Exception {
        Mockito.when(reviewService.createReview(eq(bookId), eq(review.getUser().getId()), any(Review.class)))
                .thenReturn(review);

        mockMvc.perform(post("/books/{bookId}/reviews", bookId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetReviews() throws Exception {
        Mockito.when(reviewRepository.findByBookId(bookId))
                .thenReturn(List.of(review));

        mockMvc.perform(get("/books/{bookId}/reviews", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/books/{bookId}/reviews/{reviewId}", bookId, 1L))
                .andExpect(status().isOk());

        Mockito.verify(reviewRepository).deleteByIdAndBookId(1L, bookId);
    }
}