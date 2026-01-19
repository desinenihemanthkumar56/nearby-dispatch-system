# Nearby Dispatch System 🚀

**Nearby Order Discovery & Captain Matching (Swiggy/Zomato/Rapido style)**
Spring Boot backend that reduces **long pickup distance**, improves **order acceptance rate**, and helps delivery partners earn more by saving fuel + time.
---
## 📌 Problem Statement (Real World)

Delivery partners (captains) face a major problem in hyperlocal delivery platforms:

✅ **No pay for pickup distance** (pickup travel is often unpaid)
✅ Orders show up even if pickup is **too far**, so captains reject
✅ Rejections reduce earnings and waste fuel
✅ Longer pickup → delayed delivery → customer cancellation risk
✅ Platforms struggle with **low acceptance rate** and **slow order assignment**

### 🔥 What delivery partners lose without this system

* Fuel wasted on long pickups
* Time wasted reaching pickup
* Lower daily completed orders
* Lower income because fewer orders are accepted

---

## ✅ Solution: Nearby Dispatch System

This project builds a **Nearby Order Discovery & Allocation System** that:

✅ Shows captains only the **nearest pickup orders** (default radius: **1.0 km**)
✅ Increases acceptance by reducing pickup travel
✅ Prevents duplicate assignment using **concurrency-safe order acceptance**
✅ Supports **reject + cooldown**, so captains don’t get spammed with same order
✅ Tracks assignment activity using logs (Admin visibility)

---

## 🎯 Expected Impact (Business + Captain Benefits)

This system is designed to improve key KPIs for platforms and captains:

### ✅ Platform KPIs (Company Impact)

* 📈 **Order Acceptance Rate**: +15% to +30%
* 📉 **Average Pickup Distance**: -20% to -40%
* ⚡ **Time to Assign Order**: < 15 seconds (target design)
* ❌ **Cancellations & Late Deliveries**: reduced significantly

### ✅ Captain Benefits (Delivery Partner Impact)

* ⛽ Less fuel waste (short pickup distance)
* ⏳ Time saved per trip → more deliveries/day
* 💰 Higher income because **more orders get accepted** and completed

> Example: If a captain completes 18 orders/day and acceptance improves by 20%,
> they can potentially complete **2–4 extra orders/day** depending on city demand and availability.

---

## 🧠 Core Features

### ✅ Captain Features

* Captain can go **ONLINE / OFFLINE**
* Captain sends **live location updates**
* Captain fetches nearby orders within a configurable radius (**1km / 1.5km**)
* Captain can **Accept** an order (only one captain can win)
* Captain can **Reject** an order (cooldown prevents re-showing)

### ✅ Order Features

* Create order with pickup location
* Order lifecycle:
  `NEW → ASSIGNED → PICKED → DELIVERED / CANCELLED`

### ✅ Admin Features

* View orders by status (NEW / ASSIGNED)
* View order details
* View assignment logs:
  `SENT / ACCEPTED / REJECTED / TIMEOUT`

---

## 🔐 Concurrency Safety (Industry Level)

This system prevents duplicate assignment using **atomic DB update**:

✅ If 2 captains accept the same order at the same time:

* Only 1 succeeds
* Other gets `409 CONFLICT` → `ORDER_ALREADY_ASSIGNED`

This is a core dispatch requirement in real delivery apps.

---

## 🛠️ Tech Stack

* Java 17
* Spring Boot
* Spring Security (basic config for dev)
* Spring Data JPA (Hibernate)
* MySQL
* Swagger OpenAPI (API Documentation)

---

# Nearby Dispatch System (Captain Matching)

## 📌 System Overview
This service matches online captains to nearby NEW orders within a radius (default 1.5km).
Supports concurrency-safe order assignment.

## 🔥 Swagger API Docs
![Swagger Screenshot](Docs/Images/swagger/swagger.png)

## 🗄️ Database Schema (ERD)
![DB Schema](Docs/Images/db/erd.png)

## ✅ Sample API Responses
### Nearby Orders
![Nearby Orders](Docs/Images/api-response/nearby-orders.png)

### Accept Order (Concurrency Safe)
![Accept Order](Docs/Images/api-response/order-accept.png)


## 🗄️ Database Design (Minimum)

### Captain

* id, name, status (ONLINE/OFFLINE)
* lat, lng
* lastUpdatedAt
* activeOrdersCount

### Orders

* id, orderNo
* pickupLat, pickupLng
* status (NEW/ASSIGNED/...)
* assignedCaptainId
* createdAt

### Assignment Logs

* orderId, captainId
* distanceKm
* action (SENT/ACCEPTED/REJECTED/TIMEOUT)
* createdAt

### Order Rejections

* orderId, captainId
* rejectedAt, expiresAt (cooldown)

---

## 📑 API Documentation (Swagger)

Once the project is running, open:

✅ Swagger UI
`http://localhost:8080/swagger-ui/index.html`

✅ OpenAPI JSON
`http://localhost:8080/v3/api-docs`

---

## 🔌 API Endpoints

### ✅ Captain APIs

**Create Captain**
`POST /api/captains`

```json
{
  "name": "Charan",
  "status": "ONLINE",
  "lat": 13.6288,
  "lng": 79.4192
}
```

**Get Captain By ID**
`GET /api/captains/{captainId}`

**Update Captain Status**
`POST /api/captains/{captainId}/status`

```json
{
  "status": "ONLINE"
}
```

**Update Captain Location**
`POST /api/captains/{captainId}/location`

```json
{
  "lat": 13.6288,
  "lng": 79.4192
}
```

**Get Nearby Orders**
`GET /api/captains/{captainId}/nearby-orders?radiusKm=1.0&limit=20`

---

### ✅ Order APIs

**Create Order**
`POST /api/orders`

```json
{
  "orderNo": "ORD101",
  "pickupLat": 13.6292,
  "pickupLng": 79.4201
}
```

**Accept Order** (Concurrency-safe)
`POST /api/orders/{orderId}/accept`

```json
{
  "captainId": 3
}
```

**Reject Order** (Cooldown)
`POST /api/orders/{orderId}/reject`

```json
{
  "captainId": 3
}
```

---

### ✅ Admin APIs

**List Orders by Status**
`GET /api/admin/orders?status=NEW`
`GET /api/admin/orders?status=ASSIGNED`

**Get Order Details**
`GET /api/admin/orders/{orderId}`

**Assignment Logs (Tracking)**
`GET /api/admin/assignment-logs?orderId={orderId}`

---

## ▶️ How to Run Locally

### ✅ Prerequisites

* Java 17
* MySQL running locally

### ✅ Configure DB

Update your `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nearby_dispatch
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### ✅ Run Application

```bash
mvn clean install
mvn spring-boot:run
```

---

## ✅ Testing Flow (Recommended)

1. Create Captain
2. Update Captain Location
3. Create Order
4. Get Nearby Orders
5. Accept Order
6. Check Admin Assigned Orders
7. Verify Logs

---
## 👤 Author

Hemanth Kumar Desineni
Backend Developer | Java + Spring Boot | System Design Projects

