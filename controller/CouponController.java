package com.coupons.management.coupons.controller;

import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    @Autowired
    CouponService couponService;
    private String id;

    @PostMapping
    public Coupon create(@RequestBody Map<String, Object> requestInputs) {
        return couponService.create(requestInputs);

    }

    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable Long id){
        return couponService.getCouponById(id);
    }

    @PutMapping("/{id}")
    public String updateCouponById(@PathVariable Long id, @RequestBody Coupon coupon){
        return couponService.updateCouponById(id, coupon);
    }

    @DeleteMapping("/{id}")
    public String deleteCouponById(@PathVariable Long id){
        return couponService.deleteCouponById(id);
    }




}
