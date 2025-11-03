package com.coupons.management.coupons.service.serviceImp;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.service.CouponApplyService;
import com.coupons.management.coupons.strategy.CouponRegistry;
import com.coupons.management.coupons.strategy.CouponStrategy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CouponApplyServiceImp implements CouponApplyService {
    private final static Logger logger = LoggerFactory.getLogger(CouponApplyServiceImp.class);

    @Autowired
    CouponRegistry couponRegistry;
    @Autowired
    CouponServiceImp couponServiceImp;
    @Override
    public Map<String, Object> couponApplicable(Cart cart) {
        logger.debug("isCouponApplicable is started in CouponApplyServiceImp...");
        Map<String, Object> response = new HashMap<>();
        try{
            JSONArray jsonArray = new JSONArray();
            List<Coupon> couponList = couponServiceImp.getAllCoupons();
            List<Coupon> validCoupons = getValidCoupons(couponList);
            //list of the valid or active coupon
            logger.debug("Valid Coupon List is: {}", validCoupons);
            fetchCouponDiscount(cart, validCoupons, jsonArray);
            response.put("applicable_coupons", jsonArray);
        } catch (Exception e){
            logger.error("Exception occurred while getting coupon discount.", e);
        }
        logger.debug("Total discount response is: {}", response);
        logger.debug("isCouponApplicable is ended in CouponApplyServiceImp...");
        return response;
    }

    private List<Coupon> getValidCoupons(List<Coupon> couponList){
        List<Coupon> validCouponList = new ArrayList<>();
        for(Coupon coupon : couponList){
            if(coupon.isCouponValid()){
                validCouponList.add(coupon);
            }
        }
        return validCouponList;
    }
    private void fetchCouponDiscount(Cart cart, List<Coupon> couponList, JSONArray jsonArray) {
        logger.debug("fetchCouponDiscount is started in CouponApplyServiceImp...");
        JSONObject jsonObject = new JSONObject();
        Map<String, CouponStrategy> couponStrategies = couponRegistry.getMap();
        for(Coupon coupon : couponList){
            for(CouponStrategy couponStrategy : couponStrategies.values()){
                if(couponStrategy.isApplicable(cart, coupon)){
                    double discountAmtForCoupon = couponStrategy.applyDiscount(cart, coupon);
                    jsonObject.put("coupon_id", coupon.getId());
                    jsonObject.put("type", coupon.getType());
                    jsonObject.put("discount", discountAmtForCoupon);
                    logger.debug("Obj for coupon for coupon id: {} is: {}", coupon.getId(), jsonObject);
                    jsonArray.put(jsonObject);
                }
            }

        }
        logger.debug("fetchCouponDiscount is ended in CouponApplyServiceImp...");
    }

    @Override
    public Map<String, Object> getDiscount(long id, Cart cart) {
        Map<String, Object> responseMap = new HashMap<>();
        Coupon coupon = couponServiceImp.getCouponById(id);
        CouponStrategy couponStrategy = couponRegistry.getStrategy(coupon.getType());
        boolean isApplicable = couponStrategy.isApplicable(cart, coupon);
        if(isApplicable){
            double discount = couponStrategy.applyDiscount(cart, coupon);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("items", cart.getItems());
        responseMap.put("updated_cart", jsonObject);
        return responseMap;
    }
}
