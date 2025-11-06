package com.coupons.management.coupons.strategy;

import com.coupons.management.coupons.model.Cart;
import com.coupons.management.coupons.model.Coupon;
import com.coupons.management.coupons.model.Item;
import com.coupons.management.coupons.service.serviceImp.CouponApplyServiceImp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.coupons.management.coupons.constant.Constant.BxGy_TYPE_COUPON;


public class BxGyStrategy extends CouponStrategy {
    private final static Logger logger = LoggerFactory.getLogger(BxGyStrategy.class);


    @Override
    public boolean validate(Cart cart, Coupon coupon) {
        if (!BxGy_TYPE_COUPON.equalsIgnoreCase(coupon.getType())) {
            return false;
        }

        Map<String, Object> details = coupon.getDetails();

        List<Map<String, Object>> buyProdList = (List<Map<String, Object>>) details.get("buy_products");
        Object repetitionLimitObj = details.get("repition_limit"); // fixed key spelling
        int repetitionLimit = repetitionLimitObj != null ? Integer.parseInt(repetitionLimitObj.toString()) : 1;

        int buyCount = Integer.MAX_VALUE; // we will take the min across products (complete set availability)

        for (Map<String, Object> buy : buyProdList) {
            long productId = Long.parseLong(buy.get("product_id").toString());
            int requiredQty = Integer.parseInt(buy.get("quantity").toString());

            // count how many such items are in the cart
            int itemCount = 0;
            for (Item item : cart.getItems()) {
                if (item.getId() == productId) {
                    itemCount = item.getQuantity();
                    break;
                }
            }

            // find how many full sets this product contributes
            int fullSets = itemCount / requiredQty;
            buyCount = Math.min(buyCount, fullSets);
        }

        boolean isValid = buyCount > 0 && buyCount <= repetitionLimit;

        logger.info("BxGy Validation for coupon {}: buyCount={}, repetitionLimit={}, result={}",
                coupon.getId(), buyCount, repetitionLimit, isValid);

        return isValid;
    }


    @Override
    public double applyDiscount(Cart cart, Coupon coupon) {
        Map<String, Object> details = coupon.getDetails();

        List<Map<String, Object>> buyProdList = (List<Map<String, Object>>) details.get("buy_products");
        List<Map<String, Object>> getProdList = (List<Map<String, Object>>) details.get("get_products");
        int repetitionLimit = Integer.parseInt(details.get("repition_limit").toString());

        // STEP 1: calculate how many full "buy sets" the cart qualifies for
        int totalSets = Integer.MAX_VALUE;

        for (Map<String, Object> buy : buyProdList) {
            long productId = Long.parseLong(buy.get("product_id").toString());
            int requiredQty = Integer.parseInt(buy.get("quantity").toString());

            int itemCount = 0;
            for (Item item : cart.getItems()) {
                if (item.getId() == productId) {
                    itemCount = item.getQuantity();
                    break;
                }
            }

            int sets = itemCount / requiredQty;
            logger.info("sets is {}", sets);
            totalSets = Math.min(totalSets, sets);
        }

        // Apply repetition limit
        logger.info("totalSets is {}", totalSets);
        int timesApplicable = Math.min(totalSets, repetitionLimit);

        if (timesApplicable <= 0) {
            logger.info("BxGy not applicable — no complete buy sets found for coupon ID {}", coupon.getId());
            return 0.0;
        }

        double discount = calculateDiscount(cart, getProdList, timesApplicable);

        logger.info("BxGy discount applied for coupon ID {}: Total discount = {}", coupon.getId(), discount);
        return discount;
    }


    private static double calculateDiscount(Cart cart, List<Map<String, Object>> getProdList, int timesApplicable) {
        double discount = 0.0;

        for (Map<String, Object> get : getProdList) {
            long productId = Long.parseLong(get.get("product_id").toString());
            int getQty = Integer.parseInt(get.get("quantity").toString());

            // find product in the cart
            for (Item item : cart.getItems()) {
                if (item.getId() == productId) {
                    int applicableFreeQty = Math.min(item.getQuantity(), getQty * timesApplicable);

                    double productDiscount = applicableFreeQty * item.getPrice();
                    item.setDiscount(item.getDiscount() + productDiscount); // accumulate if multiple coupons apply
                    discount += productDiscount;

                    logger.debug(
                            "Applied BxGy discount on product ID {}: freeQty={}, price={}, discount={}",
                            productId, applicableFreeQty, item.getPrice(), productDiscount
                    );

                    break;
                }
            }
        }

        return discount;
    }


    @Override
    public String toString() {
        return "BxGyStrategy{}";
    }
}
