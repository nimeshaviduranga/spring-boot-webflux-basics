package com.example.springbootwebfluxbasics.functionalendpoints;

import com.example.springbootwebfluxbasics.functionalendpoints.Product;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ProductRepository {

    private final Map<String, Product> products = new ConcurrentHashMap<>();

    public ProductRepository() {
        // Initialize with some products
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        String id3 = UUID.randomUUID().toString();

        products.put(id1, Product.builder()
                .id(id1)
                .name("Laptop")
                .price(1299.99)
                .category("Electronics")
                .inStock(true)
                .build());

        products.put(id2, Product.builder()
                .id(id2)
                .name("Smartphone")
                .price(899.99)
                .category("Electronics")
                .inStock(true)
                .build());

        products.put(id3, Product.builder()
                .id(id3)
                .name("Coffee Maker")
                .price(99.99)
                .category("Kitchen")
                .inStock(false)
                .build());
    }

    public Flux<Product> findAll() {
        return Flux.fromIterable(products.values());
    }

    public Mono<Product> findById(String id) {
        return Mono.justOrEmpty(products.get(id));
    }

    public Flux<Product> findByCategory(String category) {
        return findAll().filter(product ->
                category.equalsIgnoreCase(product.getCategory()));
    }

    public Mono<Product> save(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        products.put(product.getId(), product);
        return Mono.just(product);
    }

    public Mono<Product> update(String id, Product product) {
        product.setId(id);
        products.put(id, product);
        return Mono.just(product);
    }

    public Mono<Void> deleteById(String id) {
        products.remove(id);
        return Mono.empty();
    }

    public Mono<Void> deleteAll() {
        products.clear();
        return Mono.empty();
    }
}