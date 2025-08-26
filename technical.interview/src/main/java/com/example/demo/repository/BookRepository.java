package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByIsbn(String isbn);
    List<Book> findByAuthor(String author);
    List<Book> findByAuthorContainingIgnoreCase(String keyword);
    List<Book> findByPriceBetween(double min, double max);
}
