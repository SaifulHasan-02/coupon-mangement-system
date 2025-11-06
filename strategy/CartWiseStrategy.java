package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import org.json.JSONObject;

import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.CART_TYPE_COUPON;
import static com.coupons.management.coupons.constant.Constant.PRODUCT_TYPE_COUPON;

public class CartWiseStrategy extends CouponStrategy{

    @Override
    public boolean validate(Cart cart, Coupon coupon) {
        if(!CART_TYPE_COUPON.equalsIgnoreCase(coupon.getType())){
            return false;
        }
        Map<String, Object> details = coupon.getDetails();
        int threshold = Integer.parseInt(details.get("threshold").toString());
        double total = cart.getTotal();
        return total > threshold;
    }

    @Override
    public double applyDiscount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        double discount = Double.parseDouble(details.get("discount").toString());
        double total = cart.getTotal();
        double productDiscount = total * discount / 100;
        for(int i = 0; i < cart.getItems().size(); i++){
            cart.getItems().get(i).setDiscount(productDiscount);
        }
        return productDiscount;
    }

    @Override
    public String toString() {
        return "CartWiseStrategy{}";
    }
}

