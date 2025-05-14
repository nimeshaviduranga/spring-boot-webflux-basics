package com.example.springbootwebfluxbasics.functionalendpoints;

import com.example.springbootwebfluxbasics.functionalendpoints.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ProductHandler handler) {
        return RouterFunctions
                .route(GET("/products").and(accept(MediaType.APPLICATION_JSON)), handler::getAllProducts)
                .andRoute(GET("/products/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getProductById)
                .andRoute(GET("/products/category/{category}").and(accept(MediaType.APPLICATION_JSON)), handler::getProductsByCategory)
                .andRoute(POST("/products").and(accept(MediaType.APPLICATION_JSON)), handler::createProduct)
                .andRoute(PUT("/products/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::updateProduct)
                .andRoute(DELETE("/products/{id}"), handler::deleteProduct)
                .andRoute(DELETE("/products"), handler::deleteAllProducts)
                .andRoute(GET("/products/stream").and(accept(MediaType.TEXT_EVENT_STREAM)), handler::streamProducts);
    }
}