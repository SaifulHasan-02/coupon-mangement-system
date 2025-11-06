package com.coupons.management.coupons.model;

import java.util.List;

public class CouponApplicableResponse {
    private List<CouponDiscountResponse> applicableCoupons;

    public List<CouponDiscountResponse> getApplicableCoupons() {
        return applicableCoupons;
    }

    public void setApplicableCoupons(List<CouponDiscountResponse> applicableCoupons) {
        this.applicableCoupons = applicableCoupons;
    }

    @Override
    public String toString() {
        return "CouponApplicableResponse{" +
                "applicableCoupons=" + applicableCoupons +
                '}';
    }
}
