# 트랜잭션 관리

> - [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> - [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> - [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> - [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> - [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> - [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> - [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

---

# 4. 트랜잭션 관리

## 1. 트랜잭션 기본 개념

### 트랜잭션이란?
- 데이터베이스의 상태를 변화시키기 위해 수행하는 작업의 단위
- `ACID`(원자성, 일관성, 격리성, 지속성) 특성을 보장

### Exposed 트랜잭션 관리
- `transaction` 블록을 사용하여 트랜잭션 관리
- Spring 통합하여 `@Transactional` 어노테이션 사용 가능
- 트랜잭션 전파, 격리 수준, 타임아웃 등 설정 가능

---

## 2. 기본 트랜잭션 사용

### transaction 블록 사용

```kotlin
// 기본적인 트랜잭션 사용
fun createUser(name: String, email: String): User {
    return transaction {
        User.new {
            this.name = name
            this.email = email
        }
    }
}

// 트랜잭션 내에서 여러 작업 수행
fun transferMoney(fromId: Int, toId: Int, amount: BigDecimal) {
    transaction {
        val fromAccount = Account.findById(fromId) ?: throw AccountNotFoundException(fromId)
        val toAccount = Account.findById(toId) ?: throw AccountNotFoundException(toId)
        
        fromAccount.balance -= amount
        toAccount.balance += amount
    }
}
```

### 트랜잭션 설정

```kotlin
// 트랜잭션 격리 수준 설정
fun updateUserWithIsolation(id: Int, name: String) {
    transaction(TransactionManager.manager.defaultIsolationLevel) {
        User.findById(id)?.let { user ->
            user.name = name
        }
    }
}

// 트랜잭션 타임아웃 설정
fun longRunningOperation() {
    transaction(transactionIsolation = TransactionIsolation.REPEATABLE_READ, timeout = 30) {
        // 장시간 실행되는 작업
    }
}
```

---

## 3. Spring 통합

### @Transactional 어노테이션 사용

```kotlin
@Service
class UserService(
    private val userDao: UserDao,
    private val orderDao: OrderDao
) {
    @Transactional
    fun createUserWithOrder(userDto: UserDto, orderDto: OrderDto): User {
        val user = userDao.create(userDto)
        orderDao.create(user.id.value, orderDto)
        return user
    }

    @Transactional(readOnly = true)
    fun getUserWithOrders(id: Int): UserWithOrdersDto {
        val user = userDao.findById(id) ?: throw UserNotFoundException(id)
        val orders = orderDao.findByUserId(id)
        return UserWithOrdersDto(user, orders)
    }
}
```

### 트랜잭션 전파 설정

```kotlin
@Service
class OrderService(
    private val orderDao: OrderDao,
    private val inventoryService: InventoryService
) {
    @Transactional(propagation = Propagation.REQUIRED)
    fun placeOrder(orderDto: OrderDto) {
        orderDao.create(orderDto)
        inventoryService.updateStock(orderDto.productId, -orderDto.quantity)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun cancelOrder(orderId: Int) {
        orderDao.updateStatus(orderId, OrderStatus.CANCELLED)
        // 독립적인 트랜잭션으로 실행
    }
}
```

---

## 4. 고급 트랜잭션 기능

### 중첩 트랜잭션

```kotlin
fun complexOperation() {
    transaction {
        // 외부 트랜잭션
        val user = User.new { name = "John" }
        
        nestedTransaction {
            // 내부 트랜잭션
            user.email = "john@example.com"
        }
    }
}
```

### 트랜잭션 롤백

```kotlin
fun safeOperation() {
    transaction {
        try {
            // 작업 수행
            User.new { name = "John" }
        } catch (e: Exception) {
            rollback() // 명시적 롤백
            throw e
        }
    }
}
```

### 트랜잭션 이벤트

```kotlin
fun operationWithEvents() {
    transaction {
        beforeCommit {
            // 커밋 전 실행
            println("About to commit transaction")
        }
        
        afterCommit {
            // 커밋 후 실행
            println("Transaction committed successfully")
        }
        
        // 작업 수행
        User.new { name = "John" }
    }
}
```

---

## 5. 트랜잭션 모범 사례

### 5.1. 트랜잭션 범위 최소화

```kotlin
// 나쁜 예: 불필요하게 긴 트랜잭션
@Transactional
fun processUserData(userId: Int) {
    val user = userDao.findById(userId)
    // 긴 작업 수행
    Thread.sleep(1000)
    userDao.update(user)
}

// 좋은 예: 트랜잭션 범위 최소화
fun processUserData(userId: Int) {
    val user = userDao.findById(userId)
    // 긴 작업 수행
    Thread.sleep(1000)
    transaction {
        userDao.update(user)
    }
}
```

### 5.2. 읽기 전용 트랜잭션

```kotlin
@Transactional(readOnly = true)
fun getReportData(): ReportData {
    // 읽기 작업만 수행
    return ReportData(
        users = userDao.findAll(),
        orders = orderDao.findAll()
    )
}
```

### 5.3. 예외 처리

```kotlin
@Transactional
fun processOrder(orderDto: OrderDto) {
    try {
        orderDao.create(orderDto)
        inventoryService.updateStock(orderDto.productId, orderDto.quantity)
    } catch (e: InsufficientStockException) {
        // 비즈니스 예외는 롤백하지 않음
        throw BusinessException("재고가 부족합니다", e)
    } catch (e: Exception) {
        // 시스템 예외는 롤백
        throw e
    }
}
```

### 5.4. 동시성 제어

```kotlin
@Transactional
fun updateUserBalance(userId: Int, amount: BigDecimal) {
    val user = userDao.findById(userId) ?: throw UserNotFoundException(userId)
    
    // 낙관적 락 사용
    user.version += 1
    user.balance += amount
}
```

---

## 6. 트랜잭션 테스트

### 단위 테스트
```kotlin
class TransactionTest {
    private lateinit var database: Database

    @BeforeEach
    fun setup() {
        database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    @Test
    fun `should rollback on exception`() {
        assertThrows<Exception> {
            transaction {
                User.new { name = "John" }
                throw Exception("Test exception")
            }
        }

        transaction {
            assertEquals(0, User.all().count())
        }
    }
}
```

### 통합 테스트
```kotlin
@SpringBootTest
class TransactionIntegrationTest {
    @Autowired
    private lateinit var userService: UserService

    @Test
    @Transactional
    fun `should maintain data consistency`() {
        val user = userService.createUser("John", "john@example.com")
        val order = userService.createOrder(user.id, OrderDto(...))
        
        assertEquals(user.id, order.userId)
        assertNotNull(order.id)
    }
}
```

---
