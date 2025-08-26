package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/interview")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElseThrow();
    }

    @GetMapping("/isbn/{isbn}")
    public Book getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @GetMapping("/author/{author}")
    public List<Book> getBooksByAuthor(@PathVariable String author) {
        return bookRepository.findByAuthor(author);
    }

    @GetMapping("/author/search")
    public List<Book> searchBooksByAuthor(@RequestParam String keyword) {
        return bookRepository.findByAuthorContainingIgnoreCase(keyword);
    }

    @GetMapping("/price")
    public List<Book> getBooksByPriceRange(@RequestParam double min, @RequestParam double max) {
        return bookRepository.findByPriceBetween(min, max);
    }

    @PostMapping("/createBook")
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setPrice(updatedBook.getPrice());
        return bookRepository.save(book);
    }

    @PatchMapping("/{id}/title")
    public Book updateTitle(@PathVariable Long id, @RequestBody String title) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(title);
        return bookRepository.save(book);
    }

    @PatchMapping("/{id}/author")
    public Book updateAuthor(@PathVariable Long id, @RequestBody String author) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }
}
