# 성능 최적화

> - [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> - [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> - [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> - [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> - [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> - [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> - [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

---

# 6. 성능 최적화

## 1. 쿼리 최적화

### N+1 문제 해결

```kotlin
// 나쁜 예: N+1 문제 발생
fun findUsersWithOrders(): List<UserWithOrders> {
    return transaction {
        Users.selectAll().map { userRow ->
            val user = User.wrapRow(userRow)
            val orders = Orders.select { Orders.userId eq user.id }.map { Order.wrapRow(it) }
            UserWithOrders(user, orders)
        }
    }
}

// 좋은 예: JOIN 사용
fun findUsersWithOrdersOptimized(): List<UserWithOrders> {
    return transaction {
        (Users leftJoin Orders)
            .selectAll()
            .groupBy { it[Users.id] }
            .map { (userId, rows) ->
                val user = User.wrapRow(rows.first())
                val orders = rows.map { Order.wrapRow(it) }
                UserWithOrders(user, orders)
            }
    }
}
```

### 인덱스 활용

```kotlin
// 인덱스 생성
object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val email = varchar("email", 100).uniqueIndex()
    val status = enumerationByName("status", 20, UserStatus::class).index()
    val createdAt = datetime("created_at").index()
    
    // 복합 인덱스
    init {
        index(isUnique = true, columns = *arrayOf(name, email))
    }
}

// 인덱스 활용 쿼리
fun findUsersByNameAndStatus(name: String, status: UserStatus): List<User> {
    return transaction {
        Users.select {
            (Users.name eq name) and (Users.status eq status)
        }.map { User.wrapRow(it) }
    }
}
```

---

## 2. 배치 처리 최적화

### 배치 INSERT

```kotlin
// 나쁜 예: 개별 INSERT
fun createUsers(users: List<UserDto>) {
    transaction {
        users.forEach { userDto ->
            User.new {
                name = userDto.name
                email = userDto.email
            }
        }
    }
}

// 좋은 예: 배치 INSERT
fun createUsersOptimized(users: List<UserDto>) {
    transaction {
        Users.batchInsert(users) { userDto ->
            this[Users.name] = userDto.name
            this[Users.email] = userDto.email
        }
    }
}
```

### 배치 UPDATE

```kotlin
// 나쁜 예: 개별 UPDATE
fun updateUserStatuses(ids: List<Int>, status: UserStatus) {
    transaction {
        ids.forEach { id ->
            User.findById(id)?.status = status
        }
    }
}

// 좋은 예: 배치 UPDATE
fun updateUserStatusesOptimized(ids: List<Int>, status: UserStatus) {
    transaction {
        Users.update({ Users.id inList ids }) {
            it[Users.status] = status
        }
    }
}
```

---

## 3. 캐싱 전략

### 메모리 캐싱

```kotlin
class CachedUserDao(
    private val delegate: UserDao,
    private val cache: Cache<Int, User>
) : UserDao by delegate {
    override fun findById(id: Int): User? {
        return cache.get(id) {
            delegate.findById(id)
        }
    }
    
    override fun updateStatus(id: Int, status: UserStatus): Boolean {
        val result = delegate.updateStatus(id, status)
        if (result) {
            cache.invalidate(id)
        }
        return result
    }
}
```

### 쿼리 결과 캐싱

```kotlin
class CachedOrderStatistics(
    private val delegate: OrderDao,
    private val cache: Cache<String, OrderStatistics>
) {
    fun getStatistics(): OrderStatistics {
        return cache.get("order_statistics") {
            delegate.getOrderStatistics()
        }
    }
    
    fun invalidateCache() {
        cache.invalidate("order_statistics")
    }
}
```

---

## 4. 페이징 최적화

### 커서 기반 페이징

```kotlin
fun findUsersAfterCursor(cursor: Int, limit: Int): List<User> {
    return transaction {
        Users.select {
            Users.id greater cursor
        }
        .limit(limit)
        .map { User.wrapRow(it) }
    }
}
```

### 오프셋 기반 페이징

```kotlin
fun findUsersWithOffset(offset: Int, limit: Int): List<User> {
    return transaction {
        Users.selectAll()
            .limit(limit, offset)
            .map { User.wrapRow(it) }
    }
}
```

---

## 5. 데이터베이스 연결 풀링

### HikariCP 설정

```kotlin
@Configuration
class DatabaseConfig {
    @Bean
    fun dataSource(): DataSource {
        return HikariDataSource(HikariConfig().apply {
            jdbcUrl = "jdbc:mysql://localhost:3306/mydb"
            username = "user"
            password = "password"
            maximumPoolSize = 10
            minimumIdle = 5
            idleTimeout = 300000
            connectionTimeout = 20000
            maxLifetime = 1200000
        })
    }
}
```

---

## 6. 모니터링과 프로파일링

### 쿼리 실행 시간 측정

```kotlin
fun <T> measureQueryTime(block: () -> T): Pair<T, Long> {
    val startTime = System.currentTimeMillis()
    val result = block()
    val endTime = System.currentTimeMillis()
    return result to (endTime - startTime)
}

fun findSlowQueries(): List<QueryExecution> {
    return transaction {
        val (users, time) = measureQueryTime {
            Users.selectAll().toList()
        }
        println("Query took $time ms")
        users
    }
}
```

### 실행 계획 확인

```kotlin
fun explainQuery() {
    transaction {
        val query = Users.selectAll()
        val explain = query.explain()
        println(explain)
    }
}
```

## 7. 성능 최적화 모범 사례

### 1. 적절한 인덱스 사용
- 자주 조회되는 컬럼에 인덱스 생성
- 복합 인덱스는 조회 패턴에 맞게 설계
- 불필요한 인덱스 제거

### 2. 쿼리 최적화
- EXPLAIN으로 실행 계획 확인
- N+1 문제 해결
- 불필요한 JOIN 제거
- 서브쿼리 최적화

### 3. 배치 처리
- 대량 데이터 처리 시 배치 사용
- 적절한 배치 크기 설정
- 트랜잭션 범위 최소화

### 4. 캐싱 전략
- 자주 조회되는 데이터 캐싱
- 적절한 캐시 만료 시간 설정
- 데이터 변경 시 캐시 무효화

### 5. 연결 풀 관리
- 적절한 풀 크기 설정
- 연결 타임아웃 설정
- 연결 누수 방지
