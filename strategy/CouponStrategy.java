package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;



public abstract class CouponStrategy {


    public abstract boolean validate(Cart cart, Coupon coupon);
    public abstract double applyDiscount(Cart cart, Coupon coupon);

    public boolean isApplicable(Cart cart, Coupon coupon){
        if(!coupon.isCouponValid()) return false;

        return validate(cart, coupon);
    }

}
