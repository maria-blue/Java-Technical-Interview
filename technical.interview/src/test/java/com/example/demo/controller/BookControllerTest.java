package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setup() {
        book1 = new Book(1L, "The Hobbit", "J.R.R. Tolkien", "ISBN1", 30.0);
        book2 = new Book(2L, "Le Petit Prince", "Antoine de Saint-Exupéry", "ISBN2", 10.0);
        book3 = new Book(3L, "Another Book", "Another Author", "ISBN3", 20.0);
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Given
        List<Book> books = Arrays.asList(book1, book2, book3);

        // When
        when(bookRepository.findAll()).thenReturn(books);

        //Then
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(books.size()));
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book1.getTitle()));
    }

    @Test
    void testGetBookByIsbn() throws Exception {
        when(bookRepository.findByIsbn("ISBN1")).thenReturn(book1);

        mockMvc.perform(get("/books/isbn/ISBN1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book1.getTitle()));
    }

    @Test
    void testGetBooksByAuthor() throws Exception {
        when(bookRepository.findByAuthor(book1.getAuthor())).thenReturn(List.of(book1));

        mockMvc.perform(get("/books/author/" + book1.getAuthor()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(book1.getTitle()));
    }

    @Test
    void testGetBooksByPriceRange() throws Exception {
        when(bookRepository.findByPriceBetween(5.0, 15.0)).thenReturn(List.of(book2));

        mockMvc.perform(get("/books/price")
                        .param("min", "5.0")
                        .param("max", "15.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(book2.getTitle()));
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        mockMvc.perform(post("/books/createBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book1.getTitle()));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book updatedBook = new Book(null, "New Title", "New Author", "ISBN1", 15.0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedBook.getTitle()));
    }

    @Test
    void testUpdateTitle() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        mockMvc.perform(patch("/books/1/title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Updated Title\""))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAuthor() throws Exception {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        mockMvc.perform(patch("/books/1/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"Updated Author\""))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookRepository).deleteById(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchBooksByAuthor() throws Exception {
        when(bookRepository.findByAuthorContainingIgnoreCase("author")).thenReturn(List.of(book3));

        mockMvc.perform(get("/books/author/search")
                        .param("keyword", "author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(book3.getTitle()));
    }
}