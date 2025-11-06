package com.coupons.management.coupons.model;

import java.util.List;

public class CartSummary {
    private List<Item> items;
    private double totalPrice;
    private double totalDiscount;
    private double finalPrice;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "CartSummary{" +
                "items=" + items +
                ", totalPrice=" + totalPrice +
                ", totalDiscount=" + totalDiscount +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
