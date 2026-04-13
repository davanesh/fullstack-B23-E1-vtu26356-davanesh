package com.davanesh.service;

import com.davanesh.exception.ResourceNotFoundException;
import com.davanesh.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Product> getAllProducts() { return products; }
    public Product getProductById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }
    public Product createProduct(Product product) {
        product.setId(counter.incrementAndGet());
        products.add(product);
        return product;
    }
    public Product updateProduct(Long id, Product productDetails) {
        Product p = getProductById(id);
        p.setName(productDetails.getName());
        p.setPrice(productDetails.getPrice());
        return p;
    }
    public void deleteProduct(Long id) {
        Product p = getProductById(id);
        products.remove(p);
    }
}
