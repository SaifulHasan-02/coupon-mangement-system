package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import org.json.JSONObject;

import java.util.Map;

public class CartWiseStrategy extends CouponStrategy{

    @Override
    public boolean validate(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        int threshold = jsonObject.getInt("threshold");
        double total = cart.getTotal();
        return total > threshold;
    }

    @Override
    public double applyDiscount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        int discount = jsonObject.getInt("discount");
        double total = cart.getTotal();
        double productDiscount = total * discount / 100;
        for(int i = 0; i < cart.getItems().size(); i++){
            cart.getItems().get(i).setDiscount(productDiscount);
        }
        return productDiscount;
    }
}
