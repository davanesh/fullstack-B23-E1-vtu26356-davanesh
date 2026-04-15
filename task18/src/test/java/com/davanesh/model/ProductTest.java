package com.davanesh.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Product model class.
 * Tests data handling: constructors, getters, setters, and bean validation.
 */
@DisplayName("Product Model Tests")
class ProductTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // ─── Constructor Tests ────────────────────────────────────────────

    @Test
    @DisplayName("No-arg constructor creates Product with null fields")
    void testNoArgConstructor() {
        Product product = new Product();
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getPrice());
    }

    @Test
    @DisplayName("Parameterized constructor sets all fields correctly")
    void testParameterizedConstructor() {
        Product product = new Product(1L, "Laptop", 999.99);
        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
    }

    // ─── Getter/Setter Tests ──────────────────────────────────────────

    @Test
    @DisplayName("setId and getId work correctly")
    void testSetAndGetId() {
        Product product = new Product();
        product.setId(42L);
        assertEquals(42L, product.getId());
    }

    @Test
    @DisplayName("setName and getName work correctly")
    void testSetAndGetName() {
        Product product = new Product();
        product.setName("Keyboard");
        assertEquals("Keyboard", product.getName());
    }

    @Test
    @DisplayName("setPrice and getPrice work correctly")
    void testSetAndGetPrice() {
        Product product = new Product();
        product.setPrice(49.99);
        assertEquals(49.99, product.getPrice());
    }

    // ─── Validation Tests ─────────────────────────────────────────────

    @Test
    @DisplayName("Valid product passes all validation constraints")
    void testValidProductHasNoViolations() {
        Product product = new Product(1L, "Mouse", 25.99);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Valid product should have no violations");
    }

    @Test
    @DisplayName("Blank name triggers @NotBlank validation error")
    void testBlankNameViolation() {
        Product product = new Product(1L, "", 25.99);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Name is mandatory")));
    }

    @Test
    @DisplayName("Null name triggers @NotBlank validation error")
    void testNullNameViolation() {
        Product product = new Product(1L, null, 25.99);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Null price triggers @NotNull validation error")
    void testNullPriceViolation() {
        Product product = new Product(1L, "Monitor", null);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Price is mandatory")));
    }

    @Test
    @DisplayName("Negative price triggers @Min validation error")
    void testNegativePriceViolation() {
        Product product = new Product(1L, "Monitor", -10.0);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Price must be positive")));
    }

    @Test
    @DisplayName("Zero price is valid (boundary case)")
    void testZeroPriceIsValid() {
        Product product = new Product(1L, "Free Item", 0.0);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Zero price should be valid");
    }
}
