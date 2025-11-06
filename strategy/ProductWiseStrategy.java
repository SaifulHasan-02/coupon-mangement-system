package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.model.Item;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.PRODUCT_TYPE_COUPON;

public class ProductWiseStrategy extends CouponStrategy{
    private final static Logger logger = LoggerFactory.getLogger(ProductWiseStrategy.class);


    @Override
    public boolean validate(Cart cart, Coupon coupon) {
        if(!PRODUCT_TYPE_COUPON.equalsIgnoreCase(coupon.getType())){
            return false;
        }
        Map<String, Object> details = coupon.getDetails();
        long couponProductId = Long.parseLong(details.get("product_id").toString());
        for (Item item : cart.getItems()) {
            Long productId = item.getId();
            if (productId != null && productId == couponProductId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double applyDiscount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();
        long couponProductId = Long.parseLong(details.get("product_id").toString());
        int discount = Integer.parseInt(details.get("discount").toString());
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
        logger.info("productDiscount is: {}", productDiscount);
        return productDiscount;
    }

    @Override
    public String toString() {
        return "ProductWiseStrategy{}";
    }
}
