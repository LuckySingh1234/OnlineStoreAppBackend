package org.example.entity;

import lombok.Data;

@Data
public class CartItem implements Comparable<CartItem> {
    private String productId;
    private int quantity;

    public CartItem(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public int compareTo(CartItem o) {
        int qtyComparison = Integer.compare(this.getQuantity(), o.getQuantity());
        if (qtyComparison != 0) {
            return qtyComparison;
        } else {
            return productId.compareTo(o.getProductId());
        }
    }
}
