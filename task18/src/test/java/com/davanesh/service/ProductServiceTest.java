package com.davanesh.service;

import com.davanesh.exception.ResourceNotFoundException;
import com.davanesh.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ProductService class.
 * Tests service logic independently without Spring context (plain JUnit 5).
 */
@DisplayName("ProductService Unit Tests")
class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        // Create a fresh service instance before each test to ensure isolation
        productService = new ProductService();
    }

    // ─── CREATE Tests ─────────────────────────────────────────────────

    @Nested
    @DisplayName("createProduct()")
    class CreateProductTests {

        @Test
        @DisplayName("Should create a product and assign an auto-generated ID")
        void testCreateProductAssignsId() {
            Product product = new Product(null, "Laptop", 999.99);
            Product created = productService.createProduct(product);

            assertNotNull(created.getId(), "Created product should have an ID");
            assertEquals("Laptop", created.getName());
            assertEquals(999.99, created.getPrice());
        }

        @Test
        @DisplayName("Should assign sequential IDs to multiple products")
        void testCreateMultipleProductsSequentialIds() {
            Product p1 = productService.createProduct(new Product(null, "Laptop", 999.99));
            Product p2 = productService.createProduct(new Product(null, "Mouse", 25.99));
            Product p3 = productService.createProduct(new Product(null, "Keyboard", 49.99));

            assertEquals(1L, p1.getId());
            assertEquals(2L, p2.getId());
            assertEquals(3L, p3.getId());
        }

        @Test
        @DisplayName("Should add product to the internal list")
        void testCreateProductAddsToList() {
            productService.createProduct(new Product(null, "Laptop", 999.99));
            List<Product> allProducts = productService.getAllProducts();

            assertEquals(1, allProducts.size());
            assertEquals("Laptop", allProducts.get(0).getName());
        }
    }

    // ─── READ Tests ───────────────────────────────────────────────────

    @Nested
    @DisplayName("getAllProducts()")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return empty list when no products exist")
        void testGetAllProductsEmpty() {
            List<Product> products = productService.getAllProducts();
            assertNotNull(products);
            assertTrue(products.isEmpty());
        }

        @Test
        @DisplayName("Should return all products after adding multiple")
        void testGetAllProductsAfterAdding() {
            productService.createProduct(new Product(null, "Laptop", 999.99));
            productService.createProduct(new Product(null, "Mouse", 25.99));
            productService.createProduct(new Product(null, "Keyboard", 49.99));

            List<Product> products = productService.getAllProducts();
            assertEquals(3, products.size());
        }
    }

    @Nested
    @DisplayName("getProductById()")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return the correct product for a valid ID")
        void testGetProductByValidId() {
            Product created = productService.createProduct(new Product(null, "Laptop", 999.99));
            Product found = productService.getProductById(created.getId());

            assertNotNull(found);
            assertEquals("Laptop", found.getName());
            assertEquals(999.99, found.getPrice());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException for non-existent ID")
        void testGetProductByInvalidId() {
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.getProductById(999L)
            );
            assertTrue(exception.getMessage().contains("999"));
        }
    }

    // ─── UPDATE Tests ─────────────────────────────────────────────────

    @Nested
    @DisplayName("updateProduct()")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update name and price of existing product")
        void testUpdateExistingProduct() {
            Product created = productService.createProduct(new Product(null, "Old Name", 100.0));
            Product updatedDetails = new Product(null, "New Name", 200.0);

            Product updated = productService.updateProduct(created.getId(), updatedDetails);

            assertEquals("New Name", updated.getName());
            assertEquals(200.0, updated.getPrice());
            assertEquals(created.getId(), updated.getId(), "ID should not change");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when updating non-existent product")
        void testUpdateNonExistentProduct() {
            Product updatedDetails = new Product(null, "New Name", 200.0);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.updateProduct(999L, updatedDetails)
            );
        }

        @Test
        @DisplayName("Updated product should be retrievable with new values")
        void testUpdateProductPersistsChanges() {
            Product created = productService.createProduct(new Product(null, "Old", 10.0));
            productService.updateProduct(created.getId(), new Product(null, "Updated", 99.0));

            Product fetched = productService.getProductById(created.getId());
            assertEquals("Updated", fetched.getName());
            assertEquals(99.0, fetched.getPrice());
        }
    }

    // ─── DELETE Tests ─────────────────────────────────────────────────

    @Nested
    @DisplayName("deleteProduct()")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete an existing product successfully")
        void testDeleteExistingProduct() {
            Product created = productService.createProduct(new Product(null, "Laptop", 999.99));
            Long id = created.getId();

            assertDoesNotThrow(() -> productService.deleteProduct(id));
            assertEquals(0, productService.getAllProducts().size());
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when deleting non-existent product")
        void testDeleteNonExistentProduct() {
            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.deleteProduct(999L)
            );
        }

        @Test
        @DisplayName("Deleted product should no longer be retrievable")
        void testDeletedProductNotRetrievable() {
            Product created = productService.createProduct(new Product(null, "Laptop", 999.99));
            Long id = created.getId();
            productService.deleteProduct(id);

            assertThrows(
                    ResourceNotFoundException.class,
                    () -> productService.getProductById(id)
            );
        }

        @Test
        @DisplayName("Deleting one product should not affect others")
        void testDeleteOneProductKeepsOthers() {
            Product p1 = productService.createProduct(new Product(null, "Laptop", 999.99));
            Product p2 = productService.createProduct(new Product(null, "Mouse", 25.99));

            productService.deleteProduct(p1.getId());

            assertEquals(1, productService.getAllProducts().size());
            assertEquals("Mouse", productService.getAllProducts().get(0).getName());
        }
    }
}
