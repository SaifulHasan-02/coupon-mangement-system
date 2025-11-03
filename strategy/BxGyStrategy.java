package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.model.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Optional;


public class BxGyStrategy implements CouponStrategy{

    @Override
    public boolean isApplicable(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject = (JSONObject) details.get("details");
        JSONArray buyProdArray = jsonObject.getJSONArray("buy_products");
        int repetitionLimit = jsonObject.getInt("repition_limit");

        int buyCount = 0;
        for (int i = 0; i < buyProdArray.length(); i++) {
            JSONObject buy = buyProdArray.getJSONObject(i);
            long productId = buy.getLong("product_id");
            int requiredQty = buy.getInt("quantity");

            // count how many such items in the cart
            int itemCount = 0;
            for(Item item : cart.getItems()){
                if(item.getId() == productId){
                    itemCount = item.getQuantity();
                    break;
                }
            }
            buyCount += itemCount / requiredQty;

        }

        // Check if at least one full buy set exists
        return buyCount > 0 && repetitionLimit > 0;
    }

    @Override
    public double discount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject = (JSONObject) details.get("details");
        JSONArray buyProdArray = jsonObject.getJSONArray("buy_products");
        JSONArray getProdArray = jsonObject.getJSONArray("get_products");
        int repetitionLimit = jsonObject.getInt("repition_limit");

        // STEP 1: calculate how many "buy sets" the cart qualifies for
        int totalSets = Integer.MAX_VALUE;

        for (int i = 0; i < buyProdArray.length(); i++) {
            JSONObject buy = buyProdArray.getJSONObject(i);
            long productId = buy.getLong("product_id");
            int requiredQty = buy.getInt("quantity");

            int itemCount = 0;
            for(Item item : cart.getItems()){
                if(item.getId() == productId){
                    itemCount = item.getQuantity();
                    break;
                }
            }

            int sets = itemCount / requiredQty;
            totalSets = Math.min(totalSets, sets);
        }

        // Apply repetition limit
        int timesApplicable = Math.min(totalSets, repetitionLimit);

        if (timesApplicable <= 0) return 0.0;

        return calculateDiscount(cart, getProdArray, timesApplicable);
    }

    private static double calculateDiscount(Cart cart, JSONArray getProdArray, int timesApplicable) {
        // STEP 2: find free (get) items in cart
        double discount = 0.0;

        for (int i = 0; i < getProdArray.length(); i++) {
            JSONObject get = getProdArray.getJSONObject(i);
            long productId = get.getLong("product_id");
            int getQty = get.getInt("quantity");

            // find product in the cart
            Item targetItem = null;
            for (Item item : cart.getItems()) {
                if (item.getId() == productId) {
                    targetItem = item;
                    break;
                }
            }

            if (targetItem != null) {
                int applicableFreeQty = Math.min(
                        targetItem.getQuantity(),
                        getQty * timesApplicable
                );

                // discount equals the total price of free items
                discount += applicableFreeQty * targetItem.getPrice();
            }
        }

        return discount;
    }
}
