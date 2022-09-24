package com.example.backenddevtest.web.controller.product;

import com.example.backenddevtest.repository.products.ProductsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles({"default", "testing"})
class SimilarProductsControllerTest {
	@TestConfiguration
	public static class SimilarProductsControllerTestConfiguration {
		@Bean
		public ProductsRepository productsRepository() {
			return new MockProductsRepository();
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void pathValidationTest() throws Exception {
		// Trigger empty productId validation
		mockMvc.perform(get("/product/ /similar")).andExpect(status().isBadRequest());
	}

	@Test
	public void serviceIntegrationTest() throws Exception {
		// Similar identifiers to 2 are [1, 3, 4, 5]
		mockMvc.perform(get("/product/2/similar")).andDo(print()).andExpect(status().isOk());
		// Identifier 100 does not have similar identifiers, internal not found translated to empty list
		mockMvc.perform(get("/product/100/similar")).andDo(print()).andExpect(status().isOk());
		// Identifier 1, generates a internal server error
		mockMvc.perform(get("/product/1/similar")).andDo(print()).andExpect(status().is5xxServerError());
	}
}
