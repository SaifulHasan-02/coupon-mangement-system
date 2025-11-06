package com.coupons.management.coupons.model;

import java.util.List;

public class Cart {
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotal(){
        double sum = 0;
        for(Item item : items){
            sum += item.getPrice() * item.getQuantity();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items +
                '}';
    }
}
