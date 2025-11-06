package com.coupons.management.coupons.controller;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.CouponApplicableResponse;
import com.coupons.management.coupons.model.UpdatedCartResponse;
import com.coupons.management.coupons.service.CouponApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CouponApplyController {
    @Autowired
    CouponApplyService couponApplyService;
    @PostMapping("/applicable-coupons")
    public CouponApplicableResponse couponApplicable(@RequestBody Cart cart){
        return couponApplyService.couponApplicable(cart);
    }

    @PostMapping("/apply-coupon/{id}")
    public UpdatedCartResponse getDiscount(@PathVariable Long id, @RequestBody Cart cart){
        return couponApplyService.getDiscount(id, cart);
    }
}
