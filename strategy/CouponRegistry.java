package com.coupons.management.coupons.strategy;

import java.util.HashMap;
import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.*;

public class CouponRegistry {

    Map<String, CouponStrategy> map = new HashMap<>();
    CouponRegistry(){
        this.map.put(CART_TYPE_COUPON, new CartWiseStrategy());
        this.map.put(PRODUCT_TYPE_COUPON, new ProductWiseStrategy());
        this.map.put(BxGy_TYPE_COUPON, new BxGyStrategy());
    }


    public  CouponStrategy getStrategy(String type) {
       return this.map.get(type);
    }
}
