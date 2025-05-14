package com.example.springbootwebfluxbasics.annotatedcontrollers;

import com.example.springbootwebfluxbasics.annotatedcontrollers.Book;
import com.example.springbootwebfluxbasics.annotatedcontrollers.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Flux<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Mono<Book> findBookById(String id) {
        return bookRepository.findById(id);
    }

    public Flux<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    public Flux<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public Mono<Book> createBook(Book book) {
        return bookRepository.save(book);
    }

    public Mono<Book> updateBook(String id, Book book) {
        return bookRepository.findById(id)
                .flatMap(existingBook -> {
                    book.setId(id);
                    return bookRepository.save(book);
                });
    }

    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id);
    }

    public Mono<Void> deleteAllBooks() {
        return bookRepository.deleteAll();
    }
}