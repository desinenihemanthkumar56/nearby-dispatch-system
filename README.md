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
> they can potentially complete **2–4 extra orders/day** depending on demand.

---

## 🧠 Core Features

### ✅ Captain Features

* Captain can go **ONLINE / OFFLINE**
* Captain sends **live location updates**
* Captain fetches nearby orders within a configurable radius (**1km / 1.5km**)
* Captain can **Accept** an order (only one captain can win)
* Captain can **Reject** an order (cooldown prevents re-showing)

---

## 🔐 Concurrency Safety (Industry Level)

If 2 captains accept the same order at the same time:

* ✅ Only **1 succeeds**
* ❌ Other gets `409 CONFLICT (ORDER_ALREADY_ASSIGNED)`

---

## 🛠️ Tech Stack

* Java 17, Spring Boot
* Spring Data JPA (Hibernate)
* MySQL
* Swagger OpenAPI
* Docker + Docker Compose

---

## 📸 Proof (Docs / Screenshots)

✅ All proof screenshots are saved here:
📂 **[Docs/Images](Docs/Images)**

(Contains Swagger UI, ERD schema, and API response screenshots)

---

## 📑 Swagger API Documentation

After running the project:

✅ Swagger UI
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

✅ OpenAPI JSON
[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🔌 Key APIs (Quick View)

### Captain

* `POST /api/captains` (create captain)
* `GET /api/captains/{id}`
* `POST /api/captains/{id}/status`
* `POST /api/captains/{id}/location`
* `GET /api/captains/{id}/nearby-orders?radiusKm=1.0&limit=20`

### Orders

* `POST /api/orders` (create order)
* `POST /api/orders/{orderId}/accept` ✅ concurrency safe
* `POST /api/orders/{orderId}/reject` ✅ cooldown

### Admin

* `GET /api/admin/orders?status=NEW`
* `GET /api/admin/orders/{orderId}`
* `GET /api/admin/assignment-logs?orderId={orderId}`

---

## ▶️ Run Locally (Java + MySQL)

### Prerequisites

* Java 17
* MySQL running locally

### application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nearby_dispatch
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### Start

```bash
mvn clean install
mvn spring-boot:run
```

---

## 🐳 Run with Docker (Recommended)

### Start (App + MySQL)

```bash
docker compose up -d --build
```

### Check running containers

```bash
docker ps
```

### View app logs

```bash
docker logs -f nearby_dispatch_app
```

### Stop

```bash
docker compose down
```

App URL: [http://localhost:8080](http://localhost:8080)
Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
MySQL (host): localhost:3307

---

## ✅ Quick Test Flow

1. Create Captain
2. Update Captain Location
3. Create Order
4. Fetch Nearby Orders
5. Accept Order (verify only 1 captain wins)
6. Check Admin logs

---

## 👤 Author

**Hemanth Kumar Desineni**
Backend Developer | Java + Spring Boot | System Design Projects


