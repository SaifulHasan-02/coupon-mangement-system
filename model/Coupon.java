package com.coupons.management.coupons.model;

import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.ACTIVE;

public class Coupon {
    private Long id;
    private String type; // "cart-wise", "product-wise", "bxgy"

    private String status;

    private Map<String, Object> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public boolean isCouponValid() {
        return ACTIVE.equalsIgnoreCase(this.getStatus());
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", details=" + details +
                '}';
    }
}
