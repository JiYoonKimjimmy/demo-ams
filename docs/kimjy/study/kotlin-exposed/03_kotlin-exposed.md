# Exposed DAO 패턴 활용

> - [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> - [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> - [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> - [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> - [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> - [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> - [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

---

# 3. Exposed DAO 패턴 활용

## 1. DAO 패턴 개요

### DAO 패턴이란?
- `Data Access Object` 약자로, 데이터베이스 접근 로직을 캡슐화하는 디자인 패턴
- 비즈니스 로직과 데이터 접근 로직을 분리하여 코드의 유지보수성과 재사용성을 높임

### Exposed에서의 DAO 패턴
- Exposed는 DAO 패턴을 구현하기 위한 다양한 기능을 제공
- Table 클래스를 확장하여 DAO 클래스를 생성
- CRUD 작업을 위한 기본 메서드 제공

## 2. 기본 DAO 구현

### DAO 클래스 생성
```kotlin
// User 테이블 정의
object Users : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 100).uniqueIndex()
    val status = enumerationByName("status", 20, UserStatus::class)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}

// User 엔티티 클래스
class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var email by Users.email
    var status by Users.status
    val createdAt by Users.createdAt
}

// User DAO 클래스
class UserDao {
    // Create
    fun create(name: String, email: String, status: UserStatus = UserStatus.ACTIVE): User {
        return User.new {
            this.name = name
            this.email = email
            this.status = status
        }
    }

    // Read
    fun findById(id: Int): User? = User.findById(id)
    
    fun findByEmail(email: String): User? = User.find { Users.email eq email }.firstOrNull()
    
    fun findAllActive(): List<User> = User.find { Users.status eq UserStatus.ACTIVE }.toList()

    // Update
    fun updateStatus(id: Int, status: UserStatus): Boolean {
        return User.findById(id)?.let { user ->
            user.status = status
            true
        } ?: false
    }

    // Delete
    fun delete(id: Int): Boolean {
        return User.findById(id)?.delete() ?: false
    }
}
```

### DAO 사용 예시
```kotlin
class UserService(private val userDao: UserDao) {
    fun registerUser(name: String, email: String): User {
        return userDao.create(name, email)
    }

    fun deactivateUser(id: Int): Boolean {
        return userDao.updateStatus(id, UserStatus.INACTIVE)
    }

    fun getUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }
}
```

## 3. 고급 DAO 기능

### 트랜잭션 관리
```kotlin
class UserDao {
    fun createWithTransaction(name: String, email: String): User {
        return transaction {
            User.new {
                this.name = name
                this.email = email
            }
        }
    }

    fun updateWithTransaction(id: Int, block: User.() -> Unit): Boolean {
        return transaction {
            User.findById(id)?.apply(block)?.let { true } ?: false
        }
    }
}
```

### 배치 처리
```kotlin
class UserDao {
    fun createBatch(users: List<Pair<String, String>>): List<User> {
        return transaction {
            users.map { (name, email) ->
                User.new {
                    this.name = name
                    this.email = email
                }
            }
        }
    }

    fun updateBatch(ids: List<Int>, status: UserStatus): Int {
        return transaction {
            User.find { Users.id inList ids }
                .onEach { it.status = status }
                .count()
        }
    }
}
```

### 복잡한 쿼리
```kotlin
class UserDao {
    fun findUsersWithOrders(): List<Pair<User, Int>> {
        return transaction {
            User.all()
                .join(Orders, { it.id }, { it.userId })
                .groupBy { it[Users.id] }
                .map { (userId, rows) ->
                    val user = User[userId]
                    val orderCount = rows.count()
                    user to orderCount
                }
        }
    }

    fun findInactiveUsersWithNoOrders(): List<User> {
        return transaction {
            User.find {
                (Users.status eq UserStatus.INACTIVE) and
                (Users.id notInSubQuery Orders.slice(Orders.userId).selectAll())
            }.toList()
        }
    }
}
```

## 4. DAO 패턴 모범 사례

### 1. 단일 책임 원칙
- 각 DAO는 하나의 엔티티에 대한 CRUD 작업만 담당
- 복잡한 비즈니스 로직은 Service 레이어에서 처리

### 2. 의존성 주입
```kotlin
class UserService @Inject constructor(
    private val userDao: UserDao,
    private val orderDao: OrderDao
) {
    // ...
}
```

### 3. 예외 처리
```kotlin
class UserDao {
    fun create(name: String, email: String): User {
        return try {
            User.new {
                this.name = name
                this.email = email
            }
        } catch (e: ExposedSQLException) {
            when {
                e.message?.contains("unique constraint") == true -> 
                    throw DuplicateEmailException(email)
                else -> throw DatabaseException("Failed to create user", e)
            }
        }
    }
}
```

### 4. 캐싱 전략
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
}
```

## 5. DAO 테스트

### 단위 테스트
```kotlin
class UserDaoTest {
    private lateinit var database: Database
    private lateinit var userDao: UserDao

    @BeforeEach
    fun setup() {
        database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
        transaction(database) {
            SchemaUtils.create(Users)
        }
        userDao = UserDao()
    }

    @Test
    fun `create user should return new user`() {
        val user = userDao.create("John Doe", "john@example.com")
        
        assertNotNull(user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john@example.com", user.email)
    }

    @Test
    fun `find by email should return correct user`() {
        userDao.create("John Doe", "john@example.com")
        
        val found = userDao.findByEmail("john@example.com")
        
        assertNotNull(found)
        assertEquals("John Doe", found?.name)
    }
}
```

### 통합 테스트
```kotlin
@SpringBootTest
class UserDaoIntegrationTest {
    @Autowired
    private lateinit var userDao: UserDao

    @Test
    @Transactional
    fun `should handle concurrent updates correctly`() {
        val user = userDao.create("John Doe", "john@example.com")
        
        runBlocking {
            val jobs = List(10) {
                async {
                    userDao.updateStatus(user.id.value, UserStatus.ACTIVE)
                }
            }
            jobs.awaitAll()
        }
        
        val updated = userDao.findById(user.id.value)
        assertEquals(UserStatus.ACTIVE, updated?.status)
    }
}
```

---
