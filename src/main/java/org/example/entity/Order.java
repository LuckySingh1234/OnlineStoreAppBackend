package org.example.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Order {
    private String orderId;
    private String customerId;
    private Set<CartItem> cartItems;

    public Order(String orderId, String customerId, Set<CartItem> cartItems) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.cartItems = cartItems;
    }

    public double calculateTotalCost(Map<String, Product> products) {
        double totalCost = 0;
        for (CartItem item : cartItems) {
            Product product = products.get(item.getProductId());
            if (product != null) {
                totalCost += product.getPrice() * item.getQuantity();
            }
        }
        return totalCost;
    }
}
