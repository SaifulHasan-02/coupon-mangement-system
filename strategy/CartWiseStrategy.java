package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import org.apache.tomcat.util.json.JSONFilter;
import org.json.JSONObject;

import java.util.Map;

public class CartWiseStrategy implements CouponStrategy{

    @Override
    public boolean isApplicable(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        int threshold = jsonObject.getInt("threshold");
        double total = cart.getTotal();
        return total > threshold;
    }

    @Override
    public double discount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        int discount = jsonObject.getInt("discount");
        double total = cart.getTotal();
        return (total * discount) / 100;
    }
}
