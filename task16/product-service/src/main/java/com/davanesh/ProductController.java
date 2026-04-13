package com.davanesh;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id) { return "Product " + id; }
}
