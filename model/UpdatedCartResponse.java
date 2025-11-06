package com.coupons.management.coupons.model;

public class UpdatedCartResponse {
    private CartSummary updatedCart;

    public CartSummary getUpdatedCart() {
        return updatedCart;
    }

    public void setUpdatedCart(CartSummary updatedCart) {
        this.updatedCart = updatedCart;
    }

    @Override
    public String toString() {
        return "UpdatedCartResponse{" +
                "updatedCart=" + updatedCart +
                '}';
    }
}
