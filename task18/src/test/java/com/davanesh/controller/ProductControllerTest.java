package com.davanesh.controller;

import com.davanesh.exception.GlobalExceptionHandler;
import com.davanesh.exception.ResourceNotFoundException;
import com.davanesh.model.Product;
import com.davanesh.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ProductController using MockMvc + Mockito.
 * The Spring context is NOT loaded — this tests the controller in isolation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Unit Tests")
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // Standalone setup — no Spring context needed
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // ─── GET /products ────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /products")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return 200 OK with empty list when no products")
        void testGetAllProductsEmpty() throws Exception {
            when(productService.getAllProducts()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(productService, times(1)).getAllProducts();
        }

        @Test
        @DisplayName("Should return 200 OK with list of products")
        void testGetAllProductsReturnsData() throws Exception {
            Product p1 = new Product(1L, "Laptop", 999.99);
            Product p2 = new Product(2L, "Mouse", 25.99);
            when(productService.getAllProducts()).thenReturn(Arrays.asList(p1, p2));

            mockMvc.perform(get("/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name", is("Laptop")))
                    .andExpect(jsonPath("$[0].price", is(999.99)))
                    .andExpect(jsonPath("$[1].name", is("Mouse")))
                    .andExpect(jsonPath("$[1].price", is(25.99)));
        }
    }

    // ─── GET /products/{id} ───────────────────────────────────────────

    @Nested
    @DisplayName("GET /products/{id}")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return 200 OK with product for valid ID")
        void testGetProductByIdFound() throws Exception {
            Product product = new Product(1L, "Laptop", 999.99);
            when(productService.getProductById(1L)).thenReturn(product);

            mockMvc.perform(get("/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Laptop")))
                    .andExpect(jsonPath("$.price", is(999.99)));

            verify(productService).getProductById(1L);
        }

        @Test
        @DisplayName("Should return 404 Not Found for non-existent ID")
        void testGetProductByIdNotFound() throws Exception {
            when(productService.getProductById(999L))
                    .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

            mockMvc.perform(get("/products/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", containsString("999")));
        }
    }

    // ─── POST /products ───────────────────────────────────────────────

    @Nested
    @DisplayName("POST /products")
    class CreateProductTests {

        @Test
        @DisplayName("Should return 201 Created with valid product")
        void testCreateProductSuccess() throws Exception {
            Product input = new Product(null, "Keyboard", 49.99);
            Product saved = new Product(1L, "Keyboard", 49.99);
            when(productService.createProduct(any(Product.class))).thenReturn(saved);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("Keyboard")))
                    .andExpect(jsonPath("$.price", is(49.99)));

            verify(productService).createProduct(any(Product.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when name is blank")
        void testCreateProductBlankName() throws Exception {
            Product invalid = new Product(null, "", 49.99);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.name", is("Name is mandatory")));

            verify(productService, never()).createProduct(any());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when price is negative")
        void testCreateProductNegativePrice() throws Exception {
            Product invalid = new Product(null, "Monitor", -10.0);

            mockMvc.perform(post("/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.price", is("Price must be positive")));

            verify(productService, never()).createProduct(any());
        }
    }

    // ─── PUT /products/{id} ───────────────────────────────────────────

    @Nested
    @DisplayName("PUT /products/{id}")
    class UpdateProductTests {

        @Test
        @DisplayName("Should return 200 OK when updating existing product")
        void testUpdateProductSuccess() throws Exception {
            Product updated = new Product(1L, "Updated Laptop", 1099.99);
            when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updated);

            mockMvc.perform(put("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Updated Laptop")))
                    .andExpect(jsonPath("$.price", is(1099.99)));

            verify(productService).updateProduct(eq(1L), any(Product.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent product")
        void testUpdateProductNotFound() throws Exception {
            Product updated = new Product(null, "Updated", 100.0);
            when(productService.updateProduct(eq(999L), any(Product.class)))
                    .thenThrow(new ResourceNotFoundException("Product not found with id: 999"));

            mockMvc.perform(put("/products/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updated)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", containsString("999")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for invalid update data")
        void testUpdateProductInvalidData() throws Exception {
            Product invalid = new Product(null, "", -5.0);

            mockMvc.perform(put("/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest());

            verify(productService, never()).updateProduct(anyLong(), any());
        }
    }

    // ─── DELETE /products/{id} ────────────────────────────────────────

    @Nested
    @DisplayName("DELETE /products/{id}")
    class DeleteProductTests {

        @Test
        @DisplayName("Should return 204 No Content for successful deletion")
        void testDeleteProductSuccess() throws Exception {
            doNothing().when(productService).deleteProduct(1L);

            mockMvc.perform(delete("/products/1"))
                    .andExpect(status().isNoContent());

            verify(productService).deleteProduct(1L);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent product")
        void testDeleteProductNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("Product not found with id: 999"))
                    .when(productService).deleteProduct(999L);

            mockMvc.perform(delete("/products/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error", containsString("999")));
        }
    }
}
