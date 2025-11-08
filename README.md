# 🧾 Coupons Management API (Spring Boot)
### Monk Commerce 2025 — Backend Developer Assignment

A RESTful Spring Boot API to manage and apply different types of discount coupons  
for an e-commerce website. Designed with flexibility to add new coupon types easily  
using the **Strategy Pattern**.

---

## 🧠 Design Scenarios, Edge Cases, Assumptions & Limitations

### 1️⃣ Scenarios Considered (and/or Implemented)

#### 🛒 Cart-wise Coupons
| Scenario | Description | Expected Behavior |
|-----------|--------------|-------------------|
| ✅ **Basic threshold** | “10% off on carts over ₹100” | Apply only if cart total > threshold |

---

#### 🧩 Product-wise Coupons
| Scenario | Description | Expected Behavior |
|-----------|--------------|-------------------|
| ✅ **Single product match** | Product A has 20% off | Only Product A’s price reduced |
| ⚠️ **Product not found** | Coupon linked to a product not in cart | Coupon not applicable |

---

#### 🎁 BxGy (Buy X Get Y) Coupons
| Scenario | Description | Expected Behavior |
|-----------|--------------|-------------------|
| ✅ **Basic case** | Buy 2 from [X,Y,Z], get 1 from [A,B,C] | Applies when both sides’ conditions met |
| ✅ **Repetition limit** | Limit = 3, buy 6 items → get 3 free | Handled correctly based on count |

---

### 2️⃣ Edge Cases & Potential Issues

| Edge Case | Example | Handling |
|------------|----------|----------|
| Empty cart | `{ items: [] }` | Returns 0 applicable coupons |
| Invalid coupon ID | `/apply-coupon/999` | Returns `404 Not Found` |
| Coupon inactive | status = “inactive” | Skipped from applicability list |
| Negative or zero discount | discount ≤ 0 | Ignored |
| Invalid coupon type | type not recognized | Returns `400 Bad Request` |
| Coupon applies to removed product | product deleted from catalog | Ignored during application |
| Database unavailable | transient failure | Returns 500 with friendly message |
| Case-sensitive coupon types | “Cart-wise” vs “cart-wise” | Normalized to lowercase internally |

---

### 3️⃣ Assumptions Made

1. A **cart** contains one or more **items**, each having `id`, `name`, `price`, and `quantity`.
2. **Only one coupon** can be applied at a time through `/apply-coupon/{id}` endpoint.
3. **All prices are in INR (₹)** and considered as `double` values.
4. **Rounding**: Discount calculations rounded to 2 decimal places.
5. For **BxGy**, “buy” and “get” arrays contain **product IDs**, not names.
6. **Coupon expiry and status** are validated during both `/applicable-coupons` and `/apply-coupon` calls.
7. **Discount precedence**:
   - Product-wise discounts apply first.
   - Then cart-wise.
   - Then BxGy.
8. Coupon type extensibility is handled via the **Strategy Design Pattern** (e.g., `CouponStrategy` interface and specific strategy classes).
9. Application uses **in-memory data store** for simplicity; can easily switch to MySQL or MongoDB.
10. The system assumes **consistent product prices** between coupon validation and application time.

---

### 4️⃣ Limitations of Current Implementation

| Limitation | Description |
|-------------|--------------|
| 🚫 No UI | Backend-only system; frontend can consume APIs |
| ⚠️ Limited validation | Only basic validations implemented for inputs |
| ⚠️ Partial BxGy support | Works with equal or fewer “get” items; no weighted logic yet |
| ⚠️ No concurrency handling | In multi-user carts, race conditions may occur during simultaneous coupon application |
| ⚠️ Coupon stacking rules | Currently handles only “best discount” logic, not configurable stacking policies |
| ⚠️ Persistence | Using in-memory data; data lost on restart (for demo purposes) |
| ⚠️ Tax & shipping | Discounts calculated only on product subtotal (excluding tax/shipping) |
| ⚠️ Currency/locale | Only INR supported, no multi-currency logic yet |
| 🚫 No authentication layer | APIs are open, can be secured later using JWT/Spring Security |

---

### 5️⃣ Suggestions for Future Improvement

- ✅ Integrate **Spring Data JPA** with MySQL or MongoDB for persistence.  
- ✅ Add **user-based coupon limits** (e.g., one-time-use, per-user usage count).  
- ✅ Add **coupon stacking policies** configuration at coupon level.  
- ✅ Introduce **priority-based coupon resolution** system.  
- ✅ Support **multi-currency and localization**.  
- ✅ Include **coupon history tracking** (audit trail).  
- ✅ Implement **rate limiting & authentication** for production APIs.  

---

### 6️⃣ Example API Flow

1. `POST /coupons` → Create coupon  
2. `GET /coupons` → List all coupons  
3. `POST /applicable-coupons` → Fetch applicable coupons for current cart  
4. `POST /apply-coupon/{id}` → Apply specific coupon and return discounted cart total  

---

### 📦 Tech Stack
- **Spring Boot**
- **Java 11**
- **Maven**
- **In-Memory Data Store (HashMap)**
- **Lombok / SLF4J Logging**
- **Postman / cURL for testing**

---

### 🧩 Design Pattern
Implemented using **Strategy Pattern** for flexible coupon type handling:
