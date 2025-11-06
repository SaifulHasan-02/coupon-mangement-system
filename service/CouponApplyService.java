package com.coupons.management.coupons.service;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.CouponApplicableResponse;
import com.coupons.management.coupons.model.UpdatedCartResponse;

import java.util.Map;

public interface CouponApplyService {

    public CouponApplicableResponse couponApplicable(Cart cart);

    public UpdatedCartResponse getDiscount(long id, Cart cart);
}
