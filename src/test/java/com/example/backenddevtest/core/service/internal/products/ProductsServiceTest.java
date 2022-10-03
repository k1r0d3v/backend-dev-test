package com.example.backenddevtest.core.service.internal.products;

import com.example.backenddevtest.core.model.ProductDetail;
import com.example.backenddevtest.repository.exception.DataAccessException;
import com.example.backenddevtest.repository.exception.DataNotFoundException;
import com.example.backenddevtest.repository.products.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProductsServiceTest {
    @MockBean
    private ProductsRepository productsRepository;

    @Autowired
    private ProductsService productsService;

    @Test
    public void internalProductsServiceTest()
    {
        ProductDetail productDetail4 = new ProductDetail();
        productDetail4.setId("4");
        productDetail4.setName("Product4");
        productDetail4.setPrice(BigDecimal.valueOf(1));
        productDetail4.setAvailability(false);

        // Mock similarIds
        when(productsRepository.getSimilarProductsIds("1")).thenReturn(Mono.error(new DataNotFoundException("")));
        when(productsRepository.getSimilarProductsIds("2")).thenReturn(Mono.error(new DataAccessException("")));
        when(productsRepository.getSimilarProductsIds("3")).thenReturn(Mono.just(List.of("4")));
        when(productsRepository.getSimilarProductsIds("4")).thenReturn(Mono.just(List.of("5")));
        when(productsRepository.getSimilarProductsIds("5")).thenReturn(Mono.just(Arrays.asList("6", "7")));
        when(productsRepository.getSimilarProductsIds("6")).thenReturn(Mono.just(Arrays.asList("7", "8")));

        // Mock product details
        when(productsRepository.getProduct("4")).thenReturn(Mono.just(productDetail4));
        when(productsRepository.getProduct("5")).thenReturn(Mono.error(new DataNotFoundException("")));
        when(productsRepository.getProduct("6")).thenReturn(Mono.error(new DataAccessException("")));
        when(productsRepository.getProduct("7")).thenReturn(Mono.error(new DataNotFoundException("")));
        when(productsRepository.getProduct("8")).thenReturn(Mono.error(new DataAccessException("")));

        // Not Found error in similarIds
        assertThrowsExactly(DataNotFoundException.class, () -> productsService.getSimilarProducts("1"));

        // Unhandled data access error in similarIds
        assertThrowsExactly(DataAccessException.class, () -> productsService.getSimilarProducts("2"));

        // Happy path
        List<ProductDetail> similarProductsTo3 = productsService.getSimilarProducts("3");
        assertFalse(similarProductsTo3.isEmpty());
        assertEquals("4", similarProductsTo3.get(0).getId());

        // Not Found on product details
        assertThrowsExactly(DataNotFoundException.class, () -> productsService.getSimilarProducts("4"));

        // Generic Exception in first item and Not Found in the second, on product details
        assertThrowsExactly(DataAccessException.class, () -> productsService.getSimilarProducts("5"));

        // Not Found in first item and Generic Exception in the second, on product details
        assertThrowsExactly(DataNotFoundException.class, () -> productsService.getSimilarProducts("6"));
    }
}
