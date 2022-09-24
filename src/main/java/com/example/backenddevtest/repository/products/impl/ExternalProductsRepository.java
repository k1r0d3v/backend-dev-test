package com.example.backenddevtest.repository.products.impl;

import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.repository.products.ProductsRepository;
import com.example.backenddevtest.repository.exception.DataAccessException;
import com.example.backenddevtest.repository.exception.DataNotFoundException;
import com.example.backenddevtest.repository.products.impl.model.ExternalProductDetail;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Products repository mock implementation (an adapter in hexagonal architecture) that communicates with
 * an external service that provides products.
 */
@Repository
public class ExternalProductsRepository implements ProductsRepository {
    private final WebClient webClient;

    public ExternalProductsRepository(WebClient.Builder webClientBuilder, ExternalProductsRepositoryConfiguration configuration) {
        // Setup default properties
        webClientBuilder = webClientBuilder
                .baseUrl(String.format("http://%s:%s", configuration.getHost(), configuration.getPort()))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE);

        // Setup connection timeouts if any
        Integer timeoutSeconds = configuration.getConnectionTimeoutSeconds();
        if (timeoutSeconds != null) {
            HttpClient httpClient = HttpClient.create()
                    .doOnConnected(conn -> conn
                            .addHandlerFirst(new ReadTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS))
                            .addHandlerFirst(new WriteTimeoutHandler(timeoutSeconds, TimeUnit.SECONDS)));

            webClientBuilder = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient));
        }

        // Build the web client
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<ProductDetail> getProduct(String productId) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path( "/product/{productId}")
                        .build(productId))
                .retrieve()
                // Map 4xx status codes to DataNotFoundException
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new DataNotFoundException(
                                String.format("Product with identifier %s not found", productId))))
                .bodyToMono(ExternalProductDetail.class)
                // Map unhandled exceptions to DataAccessException
                .onErrorMap(exception -> !(exception instanceof DataAccessException), DataAccessException::new)
                .map(ExternalProductDetail::toProductDetail);
    }

    @Override
    public Mono<List<String>> getSimilarProductsIds(String productId) {
        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path( "/product/{productId}/similarids")
                                .build(productId))
                .retrieve()
                // Map 4xx status codes to DataNotFoundException
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new DataNotFoundException(
                                String.format("Similar product identifiers to product %s not found", productId))))
                .bodyToMono(String[].class)
                // Map unhandled exceptions to DataAccessException
                .onErrorMap(exception -> !(exception instanceof DataAccessException), DataAccessException::new)
                .map(idsArray -> Arrays.stream(idsArray).toList());
    }
}
