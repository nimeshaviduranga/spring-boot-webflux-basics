package com.example.springbootwebfluxbasics.functionalendpoints;

import com.example.springbootwebfluxbasics.functionalendpoints.Product;
import com.example.springbootwebfluxbasics.functionalendpoints.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductRepository repository;
    private final Validator validator;

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> products = repository.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productMono = repository.findById(id);

        return productMono
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getProductsByCategory(ServerRequest request) {
        String category = request.pathVariable("category");
        Flux<Product> products = repository.findByCategory(category);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class)
                .doOnNext(this::validate);

        return productMono
                .flatMap(repository::save)
                .flatMap(product -> ServerResponse.created(URI.create("/products/" + product.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> existingProductMono = repository.findById(id);
        Mono<Product> newProductMono = request.bodyToMono(Product.class)
                .doOnNext(this::validate);

        return existingProductMono
                .flatMap(existingProduct -> newProductMono
                        .flatMap(newProduct -> repository.update(id, newProduct)))
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(product)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Product> productMono = repository.findById(id);

        return productMono
                .flatMap(product -> repository.deleteById(id)
                        .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAllProducts(ServerRequest request) {
        return repository.deleteAll()
                .then(ServerResponse.noContent().build());
    }

    // Stream products with Server-Sent Events
    public Mono<ServerResponse> streamProducts(ServerRequest request) {
        Flux<Product> productStream = repository.findAll()
                .delayElements(Duration.ofSeconds(1));

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(productStream, Product.class);
    }

    // Validate the product
    private void validate(Product product) {
        Errors errors = new BeanPropertyBindingResult(product, "product");
        validator.validate(product, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}