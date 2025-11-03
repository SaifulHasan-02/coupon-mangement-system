package com.coupons.management.coupons.service;

import com.coupons.management.coupons.model.Cart;

import java.util.Map;

public interface CouponApplyService {

    public Map<String, Object> isCouponApplicable(Cart cart);

    public double getDiscount(long id, Cart cart);
}
