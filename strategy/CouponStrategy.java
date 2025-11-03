package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;

public interface CouponStrategy {

    boolean isApplicable(Cart cart, Coupon coupon);
    double discount(Cart cart, Coupon coupon);
}
