package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;


public class BxGyStrategy implements CouponStrategy{

    @Override
    public boolean isApplicable(Cart cart, Coupon coupon) {
       return false;
    }

    @Override
    public double discount(Cart cart, Coupon coupon) {
        return 0;
    }
}
