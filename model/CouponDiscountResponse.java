package com.coupons.management.coupons.model;

public class CouponDiscountResponse {
    private Long couponId;
    private String type;
    private double discount;

    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    @Override
    public String toString() {
        return "CouponDiscountResponse{" +
                "couponId=" + couponId +
                ", type='" + type + '\'' +
                ", discount=" + discount +
                '}';
    }
}
