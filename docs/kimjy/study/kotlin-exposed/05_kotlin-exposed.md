# 복잡한 쿼리 작성

> - [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> - [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> - [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> - [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> - [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> - [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> - [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

---

# 5. 복잡한 쿼리 작성

## 1. 조인 쿼리

### 기본 조인

```kotlin
// INNER JOIN
fun findUsersWithOrders(): List<Pair<User, Order>> {
    return transaction {
        (Users innerJoin Orders)
            .select { Orders.userId eq Users.id }
            .map { row ->
                User.wrapRow(row) to Order.wrapRow(row)
            }
    }
}

// LEFT JOIN
fun findUsersWithOrdersLeft(): List<Pair<User, Order?>> {
    return transaction {
        (Users leftJoin Orders)
            .selectAll()
            .map { row ->
                User.wrapRow(row) to Order.wrapRow(row)
            }
    }
}

// RIGHT JOIN
fun findOrdersWithUsers(): List<Pair<Order, User?>> {
    return transaction {
        (Orders rightJoin Users)
            .selectAll()
            .map { row ->
                Order.wrapRow(row) to User.wrapRow(row)
            }
    }
}
```

### 복합 조인

```kotlin
// 여러 테이블 조인
fun findUserOrderDetails(): List<UserOrderDetail> {
    return transaction {
        (Users innerJoin Orders innerJoin OrderItems innerJoin Products)
            .select {
                (Orders.userId eq Users.id) and
                (OrderItems.orderId eq Orders.id) and
                (OrderItems.productId eq Products.id)
            }
            .map { row ->
                UserOrderDetail(
                    user = User.wrapRow(row),
                    order = Order.wrapRow(row),
                    orderItem = OrderItem.wrapRow(row),
                    product = Product.wrapRow(row)
                )
            }
    }
}
```

---

## 2. 서브쿼리

### WHERE 절 서브쿼리

```kotlin
// IN 서브쿼리
fun findUsersWithRecentOrders(): List<User> {
    return transaction {
        Users.select {
            Users.id inSubQuery Orders
                .slice(Orders.userId)
                .select { Orders.createdAt greater DateTime.now().minusDays(7) }
        }.map { User.wrapRow(it) }
    }
}

// EXISTS 서브쿼리
fun findUsersWithActiveOrders(): List<User> {
    return transaction {
        Users.select {
            exists(
                Orders.select {
                    (Orders.userId eq Users.id) and
                    (Orders.status eq OrderStatus.ACTIVE)
                }
            )
        }.map { User.wrapRow(it) }
    }
}
```

### FROM 절 서브쿼리

```kotlin
// FROM 절 서브쿼리
fun findTopSpendingUsers(): List<UserSpending> {
    return transaction {
        Users
            .innerJoin(
                Orders
                    .slice(Orders.userId, sum(Orders.totalAmount).alias("total_spent"))
                    .selectAll()
                    .groupBy(Orders.userId)
                    .alias("order_totals")
            ) { Users.id eq it[Orders.userId] }
            .selectAll()
            .map { row ->
                UserSpending(
                    user = User.wrapRow(row),
                    totalSpent = row["total_spent"] as BigDecimal
                )
            }
    }
}
```

---

## 3. 집계 함수

### 기본 집계

```kotlin
// COUNT, SUM, AVG
fun getOrderStatistics(): OrderStatistics {
    return transaction {
        val stats = Orders
            .slice(
                count(),
                sum(Orders.totalAmount),
                avg(Orders.totalAmount)
            )
            .selectAll()
            .single()

        OrderStatistics(
            totalOrders = stats[count()],
            totalAmount = stats[sum(Orders.totalAmount)],
            averageAmount = stats[avg(Orders.totalAmount)]
        )
    }
}
```

### GROUP BY

```kotlin
// 단순 GROUP BY
fun getOrdersByStatus(): Map<OrderStatus, Int> {
    return transaction {
        Orders
            .slice(Orders.status, count())
            .selectAll()
            .groupBy(Orders.status)
            .associate { row ->
                row[Orders.status] to (row[count()] as Int)
            }
    }
}

// 복합 GROUP BY
fun getMonthlySales(): List<MonthlySales> {
    return transaction {
        Orders
            .slice(
                year(Orders.createdAt).alias("year"),
                month(Orders.createdAt).alias("month"),
                sum(Orders.totalAmount).alias("total_sales")
            )
            .selectAll()
            .groupBy(
                year(Orders.createdAt),
                month(Orders.createdAt)
            )
            .map { row ->
                MonthlySales(
                    year = row["year"] as Int,
                    month = row["month"] as Int,
                    totalSales = row["total_sales"] as BigDecimal
                )
            }
    }
}
```

---

## 4. 윈도우 함수

### ROW_NUMBER

```kotlin
// ROW_NUMBER 사용
fun findTopUsersBySpending(): List<UserRank> {
    return transaction {
        Users
            .innerJoin(
                Orders
                    .slice(
                        Orders.userId,
                        sum(Orders.totalAmount).alias("total_spent"),
                        rowNumber().over(
                            orderBy(sum(Orders.totalAmount).desc())
                        ).alias("rank")
                    )
                    .selectAll()
                    .groupBy(Orders.userId)
                    .alias("user_ranks")
            ) { Users.id eq it[Orders.userId] }
            .selectAll()
            .map { row ->
                UserRank(
                    user = User.wrapRow(row),
                    totalSpent = row["total_spent"] as BigDecimal,
                    rank = row["rank"] as Int
                )
            }
    }
}
```

### RANK, DENSE_RANK

```kotlin
// RANK, DENSE_RANK 사용
fun findUserRankings(): List<UserRanking> {
    return transaction {
        Users
            .innerJoin(
                Orders
                    .slice(
                        Orders.userId,
                        sum(Orders.totalAmount).alias("total_spent"),
                        rank().over(
                            orderBy(sum(Orders.totalAmount).desc())
                        ).alias("rank"),
                        denseRank().over(
                            orderBy(sum(Orders.totalAmount).desc())
                        ).alias("dense_rank")
                    )
                    .selectAll()
                    .groupBy(Orders.userId)
                    .alias("user_rankings")
            ) { Users.id eq it[Orders.userId] }
            .selectAll()
            .map { row ->
                UserRanking(
                    user = User.wrapRow(row),
                    totalSpent = row["total_spent"] as BigDecimal,
                    rank = row["rank"] as Int,
                    denseRank = row["dense_rank"] as Int
                )
            }
    }
}
```

---

## 5. CASE 표현식

### 단순 CASE

```kotlin
// 단순 CASE
fun categorizeUsers(): List<UserCategory> {
    return transaction {
        Users
            .slice(
                Users.id,
                Users.name,
                case()
                    .When(Users.status eq UserStatus.ACTIVE, "Active")
                    .When(Users.status eq UserStatus.INACTIVE, "Inactive")
                    .Else("Unknown")
                    .alias("category")
            )
            .selectAll()
            .map { row ->
                UserCategory(
                    user = User.wrapRow(row),
                    category = row["category"] as String
                )
            }
    }
}
```

### 검색 CASE

```kotlin
// 검색 CASE
fun calculateOrderTiers(): List<OrderTier> {
    return transaction {
        Orders
            .slice(
                Orders.id,
                Orders.totalAmount,
                case()
                    .When(Orders.totalAmount greater BigDecimal(1000), "Premium")
                    .When(Orders.totalAmount greater BigDecimal(500), "Standard")
                    .Else("Basic")
                    .alias("tier")
            )
            .selectAll()
            .map { row ->
                OrderTier(
                    order = Order.wrapRow(row),
                    tier = row["tier"] as String
                )
            }
    }
}
```

---

## 6. 공통 테이블 표현식(CTE)

### WITH 절 사용

```kotlin
// WITH 절 사용
fun findUserOrderHierarchy(): List<UserOrderHierarchy> {
    return transaction {
        val recentOrders = Orders
            .slice(Orders.userId, max(Orders.createdAt).alias("last_order_date"))
            .selectAll()
            .groupBy(Orders.userId)
            .alias("recent_orders")

        Users
            .innerJoin(recentOrders) { Users.id eq recentOrders[Orders.userId] }
            .selectAll()
            .map { row ->
                UserOrderHierarchy(
                    user = User.wrapRow(row),
                    lastOrderDate = row["last_order_date"] as DateTime
                )
            }
    }
}
```

### 재귀 CTE

```kotlin
// 재귀 CTE
fun findUserReferralChain(userId: Int): List<UserReferral> {
    return transaction {
        val referralChain = Users
            .slice(Users.id, Users.referredBy, literal(0).alias("level"))
            .select { Users.id eq userId }
            .unionAll(
                Users
                    .slice(Users.id, Users.referredBy, (referralChain["level"] + 1).alias("level"))
                    .select { Users.referredBy eq referralChain[Users.id] }
            )
            .alias("referral_chain")

        Users
            .innerJoin(referralChain) { Users.id eq referralChain[Users.id] }
            .selectAll()
            .map { row ->
                UserReferral(
                    user = User.wrapRow(row),
                    level = row["level"] as Int
                )
            }
    }
}
```

---
