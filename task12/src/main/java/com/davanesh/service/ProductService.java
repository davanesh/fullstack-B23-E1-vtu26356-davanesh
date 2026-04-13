package com.davanesh.service;

import com.davanesh.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Product> getAllProducts() { return products; }
    public Optional<Product> getProductById(Long id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst();
    }
    public Product createProduct(Product product) {
        product.setId(counter.incrementAndGet());
        products.add(product);
        return product;
    }
    public Product updateProduct(Long id, Product productDetails) {
        return getProductById(id).map(p -> {
            p.setName(productDetails.getName());
            p.setPrice(productDetails.getPrice());
            return p;
        }).orElse(null);
    }
    public boolean deleteProduct(Long id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}
