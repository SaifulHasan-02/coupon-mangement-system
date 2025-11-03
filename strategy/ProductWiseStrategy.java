package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.model.Item;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductWiseStrategy implements CouponStrategy{

    @Override
    public boolean isApplicable(Cart cart, Coupon coupon) {
        boolean isCouponValid = isCouponValid(coupon);
        if(!isCouponValid) return false;
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        long couponProductId = jsonObject.getLong("product_id");

        List<Item> itemList = cart.getItems();
        for(Item item : itemList){
            Long productId = item.getId();
            if(productId == couponProductId){
                return true;
            }
        }
        return false;
    }

    @Override
    public double discount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        long couponProductId = jsonObject.getLong("product_id");
        double productPrice = 0;
        List<Item> items = cart.getItems();
        for(Item item : items){
            if(item.getId() == couponProductId){
                productPrice = item.getPrice();
                break;
            }
        }

        int discount = jsonObject.getInt("discount");
        return productPrice * discount/100;

    }

    private boolean isCouponValid(Coupon coupon) {
        Date exp = coupon.getExpiry();
        Date now = new Date();
        return "ACTIVE".equalsIgnoreCase(coupon.getStatus()) &&
                (exp == null || now.before(exp));
    }
}
