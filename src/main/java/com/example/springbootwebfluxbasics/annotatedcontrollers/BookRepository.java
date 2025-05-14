package com.example.springbootwebfluxbasics.annotatedcontrollers;

import com.example.springbootwebfluxbasics.annotatedcontrollers.Book;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookRepository {

    private final Map<String, Book> books = new ConcurrentHashMap<>();

    public BookRepository() {
        // Initialize with some sample books
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String id3 = UUID.randomUUID().toString();

        books.put(id1, Book.builder()
                .id(id1)
                .title("The Great Gatsby")
                .author("F. Scott Fitzgerald")
                .year(1925)
                .genre("Classic")
                .description("A novel about the American Dream")
                .price(14.99)
                .available(true)
                .build());

        books.put(id2, Book.builder()
                .id(id2)
                .title("To Kill a Mockingbird")
                .author("Harper Lee")
                .year(1960)
                .genre("Fiction")
                .description("A novel about racial injustice")
                .price(12.99)
                .available(true)
                .build());

        books.put(id3, Book.builder()
                .id(id3)
                .title("1984")
                .author("George Orwell")
                .year(1949)
                .genre("Dystopian")
                .description("A novel about a totalitarian future")
                .price(11.99)
                .available(true)
                .build());
    }

    public Flux<Book> findAll() {
        return Flux.fromIterable(books.values());
    }

    public Mono<Book> findById(String id) {
        return Mono.justOrEmpty(books.get(id));
    }

    public Flux<Book> findByAuthor(String author) {
        return findAll()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author));
    }

    public Flux<Book> findByGenre(String genre) {
        return findAll()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre));
    }

    public Mono<Book> save(Book book) {
        if (book.getId() == null) {
            book.setId(UUID.randomUUID().toString());
        }
        books.put(book.getId(), book);
        return Mono.just(book);
    }

    public Mono<Void> deleteById(String id) {
        books.remove(id);
        return Mono.empty();
    }

    public Mono<Void> deleteAll() {
        books.clear();
        return Mono.empty();
    }
}