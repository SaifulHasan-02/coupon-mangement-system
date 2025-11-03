package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.model.Item;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProductWiseStrategy extends CouponStrategy{

    @Override
    public boolean validate(Cart cart, Coupon coupon) {
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
    public double applyDiscount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        JSONObject jsonObject  = (JSONObject) details.get("details");
        long couponProductId = jsonObject.getLong("product_id");
        int discount = jsonObject.getInt("discount");
        double productPrice = 0, productDiscount = 0;
        int ind = -1;
        for(int i = 0; i < cart.getItems().size(); i++){
            if(cart.getItems().get(i).getId() == couponProductId){
                productPrice = cart.getItems().get(i).getPrice();
                productDiscount = productPrice * discount/100;
                ind = i;
                break;
            }
        }
        if(ind != -1){
           cart.getItems().get(ind).setDiscount(productDiscount);
        }
        return productDiscount;
    }
}
