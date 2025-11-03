package com.coupons.management.coupons.controller;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Item;
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
    public Map<String, Object> couponApplicable(@RequestBody Cart cart){
        return couponApplyService.couponApplicable(cart);
    }

    @PostMapping("/apply-coupon/{id}")
    public Map<String, Object> getDiscount(@PathVariable Long id, @RequestBody Cart cart){
        return couponApplyService.getDiscount(id, cart);
    }
}
