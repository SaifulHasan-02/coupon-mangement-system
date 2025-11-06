package com.coupons.management.coupons.strategy;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.*;

@Service("CouponRegistry")
public class CouponRegistry {

    private final Map<String, CouponStrategy> map = new HashMap<>();

    CouponRegistry(){
        this.map.put(CART_TYPE_COUPON, new CartWiseStrategy());
        this.map.put(PRODUCT_TYPE_COUPON, new ProductWiseStrategy());
        this.map.put(BxGy_TYPE_COUPON, new BxGyStrategy());
    }

    public Map<String, CouponStrategy> getMap() {
        return this.map;
    }

    public CouponStrategy getStrategy(String type) {
       return this.map.get(type);
    }

}
