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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.*;

public class CouponApplyServiceImp implements CouponApplyService {
    private final static Logger logger = LoggerFactory.getLogger(CouponApplyServiceImp.class);

    @Autowired
    CouponRegistry couponRegistry;
    @Autowired
    CouponServiceImp couponServiceImp;
    @Override
    public Map<String, Object> isCouponApplicable(Cart cart) {
        logger.debug("isCouponApplicable is started in CouponApplyServiceImp...");
        Map<String, Object> response = new HashMap<>();
        try{
            JSONArray jsonArray = new JSONArray();
            List<Coupon> couponList = couponServiceImp.getAllCoupons();
            logger.debug("Coupon List is: {}", couponList);
            fetchCouponDiscount(cart, couponList, CART_TYPE_COUPON, jsonArray);
            fetchCouponDiscount(cart, couponList, PRODUCT_TYPE_COUPON, jsonArray);
            fetchCouponDiscount(cart, couponList, BxGy_TYPE_COUPON, jsonArray);
            response.put("applicable_coupons", jsonArray);
        } catch (Exception e){
            logger.error("Exception occurred while getting coupon discount.", e);
        }
        logger.debug("Total discount response is: {}", response);
        logger.debug("isCouponApplicable is ended in CouponApplyServiceImp...");
        return response;
    }

    private void fetchCouponDiscount(Cart cart, List<Coupon> couponList, String type, JSONArray jsonArray) {
        logger.debug("fetchCouponDiscount is started in CouponApplyServiceImp...");
        JSONObject jsonObject = new JSONObject();
        CouponStrategy cartWiseCoupon = couponRegistry.getStrategy(type);
        for(Coupon coupon : couponList){
            if(cartWiseCoupon.isApplicable(cart, coupon)){
                double discountAmtForCoupon = cartWiseCoupon.discount(cart, coupon);
                jsonObject.put("coupon_id", coupon.getId());
                jsonObject.put("type", coupon.getType());
                jsonObject.put("discount", discountAmtForCoupon);
                jsonArray.put(jsonObject);
            }
        }
        logger.debug("fetchCouponDiscount is ended in CouponApplyServiceImp...");
    }

    @Override
    public double getDiscount(long id, Cart cart) {
        return 0;
    }
}
