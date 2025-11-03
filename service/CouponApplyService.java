package com.coupons.management.coupons.service;

import com.coupons.management.coupons.model.Cart;

import java.util.Map;

public interface CouponApplyService {

    public Map<String, Object> couponApplicable(Cart cart);

    public Map<String, Object> getDiscount(long id, Cart cart);
}
