package com.coupons.management.coupons.service.serviceImp;

import com.coupons.management.coupons.model.*;
import com.coupons.management.coupons.service.CouponApplyService;
import com.coupons.management.coupons.strategy.CouponRegistry;
import com.coupons.management.coupons.strategy.CouponStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("CouponApplyService")
public class CouponApplyServiceImp implements CouponApplyService {
    private final static Logger logger = LoggerFactory.getLogger(CouponApplyServiceImp.class);

    @Autowired
    CouponRegistry couponRegistry;
    @Autowired
    CouponServiceImp couponServiceImp;

    @Override
    public CouponApplicableResponse couponApplicable(Cart cart) {
        logger.debug("isCouponApplicable is started in CouponApplyServiceImp...");
        CouponApplicableResponse response = new CouponApplicableResponse();
        try {
            logger.debug("Cart is: {}", cart);
            JSONArray jsonArray = new JSONArray();
            List<Coupon> couponList = couponServiceImp.getAllCoupons();
            List<Coupon> validCoupons = getValidCoupons(couponList);
            //list of the valid or active coupon
            logger.info("Valid Coupon List is: {}", validCoupons);
            List<CouponDiscountResponse> applicableCoupons = new ArrayList<>();
            fetchCouponDiscount(cart, validCoupons, applicableCoupons);
            response.setApplicableCoupons(applicableCoupons);
        } catch (Exception e) {
            logger.error("Exception occurred while getting coupon discount.", e);
        }
        logger.info("Total discount response is: {}", response);
        logger.info("isCouponApplicable is ended in CouponApplyServiceImp...");
        return response;
    }

    private List<Coupon> getValidCoupons(List<Coupon> couponList) {
        List<Coupon> validCouponList = new ArrayList<>();
        for (Coupon coupon : couponList) {
            if (coupon.isCouponValid()) {
                validCouponList.add(coupon);
            }
        }
        return validCouponList;
    }

    private void fetchCouponDiscount(Cart cart, List<Coupon> couponList, List<CouponDiscountResponse> applicableCoupons) {
        logger.info("fetchCouponDiscount is started in CouponApplyServiceImp...");
        Map<String, CouponStrategy> couponStrategies = couponRegistry.getMap();
        logger.info("couponStrategies is: {}", couponStrategies);
        System.out.println("couponStrategies.values(): "+couponStrategies.values());
        for (Coupon coupon : couponList) {
            for (CouponStrategy couponStrategy : couponStrategies.values()) {
                if (couponStrategy.isApplicable(cart, coupon)) {
                    double discountAmtForCoupon = couponStrategy.applyDiscount(cart, coupon);
                    logger.info("discountAmtForCoupon is : {}", discountAmtForCoupon);
                    CouponDiscountResponse discountResponse = new CouponDiscountResponse();
                    discountResponse.setCouponId(coupon.getId());
                    discountResponse.setType(coupon.getType());
                    discountResponse.setDiscount(discountAmtForCoupon);
                    applicableCoupons.add(discountResponse);
                    logger.debug("Added applicable coupon: {}", discountResponse);
                }
            }

        }
        logger.info("response is {}", applicableCoupons.size());
        logger.info("fetchCouponDiscount is ended in CouponApplyServiceImp...");
    }


    @Override
    public UpdatedCartResponse getDiscount(long id, Cart cart) {
        UpdatedCartResponse response = new UpdatedCartResponse();
        Coupon coupon = couponServiceImp.getCouponById(id);
        if(coupon.getId() == null || coupon.getId() != id){
            logger.warn("No coupon found for ID {}", id);
            return null;
        }
        CouponStrategy couponStrategy = couponRegistry.getStrategy(coupon.getType());
        if(couponStrategy == null){
            logger.warn("No coupon strategy for ID {}", id);
            return null;
        }
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double finalPrice = 0, totalDiscount = 0;
        boolean isApplicable = couponStrategy.isApplicable(cart, coupon);
        if (isApplicable) {
            totalDiscount = couponStrategy.applyDiscount(cart, coupon);
            finalPrice = totalPrice - totalDiscount;

        }
        CartSummary cartSummary = new CartSummary();
        cartSummary.setItems(cart.getItems());
        cartSummary.setTotalPrice(totalPrice);
        cartSummary.setTotalDiscount(totalDiscount);
        cartSummary.setFinalPrice(finalPrice);
        response.setUpdatedCart(cartSummary);
        logger.info("Response is: {}", response);
        return response;

    }
}
