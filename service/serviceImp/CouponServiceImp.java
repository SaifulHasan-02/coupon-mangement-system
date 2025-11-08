package com.coupons.management.coupons.service.serviceImp;

import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service("CouponService")
public class CouponServiceImp implements CouponService {
    private final static Logger logger = LoggerFactory.getLogger(CouponServiceImp.class);
    private final Map<Long, Coupon> couponStore = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    @Override
    public Coupon create(Map<String, Object> requestInputs) {
        logger.debug("create method is start in CouponServiceImp");
        try{
            logger.debug("request inputs is: {}", requestInputs);
            if (requestInputs == null || !requestInputs.containsKey("type") || !requestInputs.containsKey("details")) {
                throw new IllegalArgumentException("Missing required fields: 'type' and 'details'");
            }
            String type =  requestInputs.get("type").toString();
            String status = requestInputs.get("status").toString();
            Date exp = (Date) requestInputs.get("date");
            Map<String, Object> details = (Map<String, Object>) requestInputs.get("details");
            long newId = idCounter.getAndIncrement();
            Coupon coupon = createPayloadForCoupon(newId, type, status, exp, details);
            couponStore.put(newId, coupon);
            logger.info("Coupon created successfully with ID: {}", newId);
            return coupon;

        }catch (IllegalArgumentException e) {
            logger.error("Invalid input while creating coupon: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating coupon: ", e);
            throw new RuntimeException("Failed to create coupon", e);
        } finally {
            logger.debug("create method is end in CouponServiceImp");
        }
    }

    @Override
    public List<Coupon> getAllCoupons() {
        logger.debug("getAllCoupons  is started in CouponServiceImp");
        List<Coupon> couponList = new ArrayList<>();
        try{
            for(Map.Entry<Long, Coupon> entry : couponStore.entrySet()){
                couponList.add(entry.getValue());
            }
            logger.debug("Coupon List is: {}", couponList);

        } catch (Exception e){
            logger.error("Unexpected error while getting all coupons: ", e);
        }
        logger.debug("getAllCoupons is ended in CouponServiceImp");
        return couponList;
    }

    @Override
    public Coupon getCouponById(Long id) {
        logger.debug("getCouponById is started in CouponServiceImpl");
        try {
            for (Map.Entry<Long, Coupon> entry : couponStore.entrySet()) {
                if (Objects.equals(id, entry.getKey())) {
                    logger.debug("Coupon found for id: {}", id);
                    return entry.getValue();
                }
            }
            logger.warn("No coupon found with id: {}", id);
            return new Coupon();
        } catch (Exception e) {
            logger.error("Unexpected error while getting coupon by id: ", e);
            throw e;
        }
    }

    @Override
    public String updateCouponById(Long id, Coupon coupon) {
        logger.debug("updateCouponById is started in CouponServiceImpl");
        try{
            for (Map.Entry<Long, Coupon> entry : couponStore.entrySet()) {
                if(Objects.equals(id, entry.getKey())){
                    couponStore.put(id, coupon);
                    logger.info("Update Coupon by Coupon Id successfully.");
                    return "Coupon update successfully for coupon Id: "+ id;
                }
            }
            logger.info("No Coupon found with coupon Id: {}", id);
            return "No Coupon found with coupon Id: "+ id;
        } catch (Exception e) {
            logger.error("Error occurred while updating coupon by id: {}", id);
            throw e;
        }
    }

    @Override
    public String deleteCouponById(Long id) {
        logger.debug("deleteCouponById is started in CouponServiceImpl");
        try {
            for (Map.Entry<Long, Coupon> entry : couponStore.entrySet()) {
                if (Objects.equals(id, entry.getKey())) {
                    couponStore.remove(id);
                    logger.info("Delete Coupon by Coupon Id: {}", id);
                    return "Delete Coupon by Coupon Id:" + id;
                }
            }
            logger.info("No Coupon found with coupon Id: {}", id);
            return "Deletion failed: No Coupon found with coupon Id: "+ id;
        }catch (Exception e){
            logger.error("Error occurred while deletion coupon by id: {}", id);
            throw e;
        }
    }
    private Coupon createPayloadForCoupon(Long newId, String type, String status, Date date, Map<String, Object> details){
        Coupon coupon = new Coupon();
        coupon.setId(newId);
        coupon.setType(type);
        coupon.setStatus(status);
        coupon.setDetails(details);
        return coupon;
    }

}
