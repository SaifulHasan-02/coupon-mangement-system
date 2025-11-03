package com.coupons.management.coupons.service;

import com.coupons.management.coupons.model.Coupon;

import java.util.List;
import java.util.Map;

public interface CouponService {
    public Coupon create(Map<String, Object> requestInputs);

    public List<Coupon> getAllCoupons();

    public Coupon getCouponById(Long id);
    public String updateCouponById(Long id, Coupon coupon);
    public String deleteCouponById(Long id);

}
