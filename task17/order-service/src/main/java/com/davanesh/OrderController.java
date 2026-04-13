package com.davanesh;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ProductClient productClient;
    public OrderController(ProductClient productClient) {
        this.productClient = productClient;
    }
    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id) {
        return "Order " + id + " contains " + productClient.getProduct(id);
    }
}
