# Exposed with Spring Boot 설정

> [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

## Agenda

1. [프로젝트 환경 설정](#1-프로젝트-환경-설정)
   - Gradle 의존성 추가
   - Spring Boot 버전 호환성
   - Exposed 모듈 설명 (Core, DAO, Spring Boot Starter)

2. [데이터베이스 연결 설정](#2-데이터베이스-연결-설정)
   - application.yml 설정
   - DataSource 설정
   - 다중 DataSource 설정 방법

3. [Exposed 설정](#3-exposed-설정)
   - Spring Boot Auto-Configuration
   - 커스텀 설정 방법
   - 트랜잭션 매니저 설정

4. [Entity 매핑 설정](#4-entity-매핑-설정)
   - Table 객체 정의
   - Entity 클래스 정의
   - 관계 매핑 설정

5. [Repository 구현](#5-repository-구현)
   - Spring Data JPA 스타일 구현
   - Exposed DSL 스타일 구현
   - DAO 패턴 구현

6. [테스트 환경 설정](#6-테스트-환경-설정)
   - 테스트용 데이터베이스 설정
   - 테스트 트랜잭션 관리
   - 테스트 데이터 준비

---

## 1. 프로젝트 환경 설정

### Gradle 의존성 추가

#### build.gradle.kts

```kotlin
val exposedVersion = "0.45.0"  // 최신 버전 사용 권장

dependencies {
    // Spring Boot Starter
    implementation("org.jetbrains.exposed.spring:spring-transaction:$exposedVersion")
    
    // Exposed 핵심 모듈
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    
    // 선택적 모듈
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")  // Java time 타입 지원
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")       // JSON 타입 지원
}
```

### Spring Boot 버전 호환성

| Spring Boot 버전 | Exposed 권장 버전 | 비고              |
|----------------|---------------|-----------------|
| 3.x.x          | 0.45.0 이상     | Kotlin 1.9.x 지원 |
| 2.7.x          | 0.41.1 이상     | Kotlin 1.8.x 지원 |
| 2.6.x          | 0.37.3 이상     | Kotlin 1.6.x 지원 |

### Exposed 모듈 설명

#### 1. 핵심 모듈

- **exposed-core**
  - Exposed의 기본 기능을 제공
  - SQL DSL의 기반이 되는 클래스들 포함
  - 테이블 정의, 컬럼 타입 등 기본 구성요소 제공

- **exposed-dao**
  - DAO(Data Access Object) 패턴 구현을 위한 모듈
  - 엔티티 매핑과 CRUD 작업을 위한 기능 제공
  - JPA와 유사한 방식의 엔티티 관리 기능

- **exposed-jdbc**
  - JDBC 연결과 관련된 기능 제공
  - 데이터베이스 연결 및 트랜잭션 관리
  - SQL 실행과 결과 처리

#### 2. 확장 모듈

- **exposed-spring-boot-starter**
  - Spring Boot 통합을 위한 자동 설정 제공
  - 트랜잭션 관리와 데이터소스 설정 자동화
  - Spring의 트랜잭션 관리자와 통합

- **exposed-java-time**
  - Java 8의 날짜/시간 타입 지원
  - LocalDate, LocalDateTime 등 사용 가능
  - 시간대 처리를 위한 유틸리티 제공

- **exposed-json**
  - JSON 데이터 타입 지원
  - PostgreSQL의 JSONB 타입 지원
  - JSON 데이터 쿼리 및 조작 기능

### 모듈 선택 가이드

1. **최소 설정**
   ```kotlin
   dependencies {
       implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
   }
   ```

2. **일반적인 설정 (권장)**
   ```kotlin
   dependencies {
       // Spring 트랜잭션 통합
       implementation("org.jetbrains.exposed.spring:spring-transaction:$exposedVersion")
       // 또는 자동 설정을 선호하는 경우:
       // implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
       
       // 핵심 모듈
       implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
   }
   ```

3. **전체 기능 사용**
   ```kotlin
   dependencies {
       implementation("org.jetbrains.exposed.spring:spring-transaction:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
       implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
   }
   ```

> **참고**: `exposed-spring-boot-starter`와 `exposed-spring-transaction`의 차이
> - `exposed-spring-transaction`: Spring의 트랜잭션 관리 기능만 제공
> - `exposed-spring-boot-starter`: 자동 설정을 포함한 모든 필요한 의존성 포함
> 
> 일반적으로는 더 세밀한 제어가 가능한 `exposed-spring-transaction`을 사용하는 것을 권장합니다.
> 단, Spring Boot의 자동 설정 기능을 선호하는 경우에는 `exposed-spring-boot-starter`를 사용할 수 있습니다.

---

## 2. 데이터베이스 연결 설정

- `application.yml` 또는 `application.properties`를 통해 쉽게 설정 가능

### application.yml 설정

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # 데이터베이스 URL
    driver-class-name: org.h2.Driver  # 드라이버 클래스
    username: sa  # 데이터베이스 사용자명
    password: password  # 데이터베이스 비밀번호

    # 커넥션 풀 설정 (HikariCP)
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      max-lifetime: 1200000
```

### DataSource 설정

- `Spring Boot` 자동 설정을 사용하지 않고, 직접 `DataSource` 설정 가능

```kotlin
@Configuration
class DatabaseConfig {
    
    @Bean
    fun dataSource(): DataSource {
        return HikariDataSource().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://localhost:5432/mydb"
            username = "postgres"
            password = "postgres"
            maximumPoolSize = 10
            minimumIdle = 5
            idleTimeout = 300000
            connectionTimeout = 20000
            maxLifetime = 1200000
        }
    }
}
```

### 다중 DataSource 설정

#### application.yml

```yaml
spring:
  primary-datasource:
    url: jdbc:postgresql://localhost:5432/primary_db
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: postgres
    
  secondary-datasource:
    url: jdbc:mysql://localhost:3306/secondary_db
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
```

#### 다중 DataSource 설정 클래스

```kotlin
@Configuration
class MultipleDataSourceConfig {

    @Primary
    @Bean("primaryDataSource")
    @ConfigurationProperties("spring.primary-datasource")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean("secondaryDataSource")
    @ConfigurationProperties("spring.secondary-datasource")
    fun secondaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Primary
    @Bean("primaryDatabase")
    fun primaryDatabase(@Qualifier("primaryDataSource") dataSource: DataSource): Database {
        return Database.connect(dataSource)
    }

    @Bean("secondaryDatabase")
    fun secondaryDatabase(@Qualifier("secondaryDataSource") dataSource: DataSource): Database {
        return Database.connect(dataSource)
    }
}
```

### 데이터베이스 초기화 설정

#### Schema 자동 생성

```kotlin
@Configuration
class DatabaseInitializer {
    
    @Bean
    fun initDatabase(database: Database) {
        transaction(database) {
            // 테이블 생성
            SchemaUtils.create(Users, Roles, UserRoles)
            
            // 초기 데이터 삽입
            Users.insert {
                it[name] = "Admin"
                it[email] = "admin@example.com"
            }
        }
    }
}
```

> #### Flyway를 사용한 데이터베이스 마이그레이션
>
> - `Flyway` : 데이터베이스 스키마 버전 관리 도구로, 데이터베이스의 변경사항을 코드로 관리할 수 있도록 지원하는 라이브러리
> [Flyway 관련 블로그](./99_flyway-guide.md)

---

## 3. Exposed 설정

- `Exposed` 를 `Spring Boot` 와 함께 사용할 때는 크게 두 가지 방식으로 설정 가능
  1. Spring Boot의 자동 설정 사용 (`exposed-spring-boot-starter`)
  2. 수동 설정 (`exposed-spring-transaction`)

### Auto Configuration

- `exposed-spring-boot-starter` 를 통해 자동 설정 가능

```kotlin
dependencies {
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:$exposedVersion")
}
```

#### 자동 설정 항목

- Database 인스턴스 자동 생성
- SpringTransactionManager 설정
- 트랜잭션 매니저와 Spring 통합

### Custom Configuration

```kotlin
@Configuration
class ExposedConfig {
    
    @Bean
    fun database(dataSource: DataSource): Database {
        return Database.connect(dataSource)
    }
    
    @Bean
    fun springTransactionManager(database: Database): SpringTransactionManager {
        return SpringTransactionManager(database)
    }
}
```

#### 추가 설정 옵션

```kotlin
@Configuration
class ExposedCustomConfig {
    
    @Bean
    fun database(dataSource: DataSource): Database {
        return Database.connect(dataSource).apply {
            useNestedTransactions = true  // 중첩 트랜잭션 허용
        }
    }
    
    @Bean
    fun databaseConfig(): DatabaseConfig {
        return DatabaseConfig {
            sqlLogger = Slf4jSqlLogger    // SQL 로깅 설정
            defaultRepetitionAttempts = 3 // 재시도 횟수 설정
            defaultIsolationLevel = Connection.TRANSACTION_READ_COMMITTED // 기본 격리 수준 설정
            defaultBatchSize = 100        // 배치 작업 시 기본 크기
        }
    }

}
```

### Transaction Manager 설정

#### Default Transaction Manager

```kotlin
@Configuration
class TransactionConfig {
   
    @Bean
    @Primary
    fun transactionManager(database: Database): PlatformTransactionManager {
        return SpringTransactionManager(
            database = database,
            showSql = true,                // SQL 출력 여부
            defaultRepetitionAttempts = 3  // 재시도 횟수
        )
    }

}
```

#### Multiple Transaction Manager

- 다중 데이터베이스를 사용하는 경우, 각 Datasource 별 다른 Transaction Manager 설정 가능

```kotlin
@Configuration
class MultipleTransactionConfig {
    
    @Bean
    @Primary
    fun primaryTransactionManager(
        @Qualifier("primaryDatabase") database: Database
    ): PlatformTransactionManager {
        return SpringTransactionManager(database)
    }
    
    @Bean
    fun secondaryTransactionManager(
        @Qualifier("secondaryDatabase") database: Database
    ): PlatformTransactionManager {
        return SpringTransactionManager(database)
    }

}
```

#### Transaction 사용 Sample

```kotlin
@Service
@Transactional
class UserService(private val database: Database) {
    
    fun createUser(name: String, email: String) {
        transaction(database) {
            Users.insert {
                it[this.name] = name
                it[this.email] = email
            }
        }
    }
    
    // Spring `@Transactional` 함께 사용
    @Transactional(readOnly = true)
    fun findUserByEmail(email: String): User? {
        return transaction(database) {
            User.find { Users.email eq email }.firstOrNull()
        }
    }

}
```

> ####  **Spring** `@Transactional` 과 **Exposed** 의 `transaction`
> 
> - `@Transactional` : Spring 트랜잭션 관리 기능 사용
> - `transaction { }` : Exposed 트랜잭션 컨텍스트 생성
> 
> 두 방식을 함께 사용할 때는 **Spring 트랜잭션이 외부**에, **Exposed 트랜잭션이 내부**에 위치하도록 구성하는 것을 권장
>
> ##### 트랜잭션 중첩 구조 예시
> ```kotlin
> @Service
> class UserService(private val database: Database) {
>     
>     // 올바른 사용 방법 ✅
>     @Transactional  // Spring 트랜잭션 (외부)
>     fun createUser(name: String, email: String) {
>         transaction(database) {  // Exposed 트랜잭션 (내부)
>             Users.insert { 
>                 it[this.name] = name
>                 it[this.email] = email
>             }
>         }
>     }
>     
>     // 잘못된 사용 방법 ❌
>     fun wrongCreateUser(name: String, email: String) {
>         transaction(database) {  // Exposed 트랜잭션 (외부)
>             @Transactional      // Spring 트랜잭션 (내부) - 동작하지 않을 수 있음
>             fun insert() {
>                 Users.insert {
>                     it[this.name] = name
>                     it[this.email] = email
>                 }
>             }
>             insert()
>         }
>     }
> }
> ```
>
> ##### 이렇게 구성하는 이유
> 
> 1. **트랜잭션 전파**: Spring의 트랜잭션이 외부에 있으면 Spring의 트랜잭션 전파 설정(`propagation`)이 제대로 동작
> 2. **리소스 관리**: Spring이 트랜잭션 리소스(커넥션 등)를 먼저 확보하고, Exposed는 이를 재사용
> 3. **예외 처리**: Spring의 트랜잭션 경계에서 예외가 발생하면, 내부의 Exposed 작업도 함께 롤백 처리
> 4. **AOP 동작**: Spring `@Transactional` 은 AOP를 통해 동작하므로, 외부에 위치해야 프록시가 정상적으로 동작
>
> ##### 주의사항
> 
> - Exposed `transaction` 블록은 반드시 필요(Exposed DSL 사용을 위한 컨텍스트 제공)
>   - Exposed `transaction`: SQL DSL 실행을 위한 컨텍스트 제공
> - Spring `@Transactional` 은 선택적(트랜잭션 관리가 필요한 경우에만 사용)
>   - Spring `@Transactional`: 전체 트랜잭션 경계와 속성 관리


---

## 4. Entity 매핑 설정

1. **DSL 방식**: `Table` 객체를 사용한 **SQL DSL 스타일**
2. **DAO 방식**: `Entity` 클래스를 사용한 **ORM 스타일**

### Table 객체 정의 (DSL 방식)

```kotlin
// 단일 테이블 정의
object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 100)
    val email = varchar("email", length = 255).uniqueIndex()
    val createdAt = datetime("created_at").default(LocalDateTime.now())
    val status = enumerationByName("status", 10, UserStatus::class)
    
    override val primaryKey = PrimaryKey(id)
}

// Enum 클래스 정의
enum class UserStatus {
    ACTIVE, INACTIVE, SUSPENDED
}

// 관계를 가진 테이블 정의
object Roles : Table("roles") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 50)
    
    override val primaryKey = PrimaryKey(id)
}

object UserRoles : Table("user_roles") {
    val userId = reference("user_id", Users)
    val roleId = reference("role_id", Roles)
    
    override val primaryKey = PrimaryKey(userId, roleId)
}
```

#### Column 타입 정의

```kotlin
object SampleTable : Table("sample") {
    // 숫자 타입
    val intColumn = integer("int_column")
    val longColumn = long("long_column")
    val decimalColumn = decimal("decimal_column", precision = 10, scale = 2)
    
    // 문자열 타입
    val varcharColumn = varchar("varchar_column", length = 100)
    val textColumn = text("text_column")
    
    // 날짜/시간 타입
    val dateColumn = date("date_column")
    val dateTimeColumn = datetime("datetime_column")
    val timestampColumn = timestamp("timestamp_column")
    
    // 불리언 타입
    val booleanColumn = bool("boolean_column")
    
    // BLOB/CLOB
    val blobColumn = blob("blob_column")
    val clobColumn = clob("clob_column")
    
    // JSON 타입 (exposed-json 모듈 필요)
    val jsonColumn = json("json_column", String::class.java)
    
    // 열거형
    val enumColumn = enumerationByName("enum_column", 10, Status::class)
    
    // Nullable 컬럼
    val nullableColumn = varchar("nullable_column", 100).nullable()
    
    // 기본값 설정
    val defaultColumn = integer("default_column").default(0)
}
```

### Entity 클래스 정의 (DAO 방식)

```kotlin
// Entity 클래스 정의
class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    
    var name by Users.name
    var email by Users.email
    var createdAt by Users.createdAt
    var status by Users.status
    
    // 일대다 관계
    val roles by Role via UserRoles
}

class Role(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Role>(Roles)
    
    var name by Roles.name
    
    // 다대다 관계
    val users by User via UserRoles
}
```

### 관계 매핑 설정

#### 1. 일대일 (One-to-One) 관계

```kotlin
object UserProfiles : Table("user_profiles") {
    val id = integer("id").autoIncrement()
    val userId = reference("user_id", Users).uniqueIndex()
    val bio = text("bio")
    
    override val primaryKey = PrimaryKey(id)
}

class UserProfile(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserProfile>(UserProfiles)
    
    var user by User referencedOn UserProfiles.userId
    var bio by UserProfiles.bio
}

// User 클래스에 추가
class User(id: EntityID<Int>) : IntEntity(id) {
    // ... 기존 속성들 ...
    val profile by UserProfile optionalReferrersOn UserProfiles.userId
}
```

#### 2. 일대다 (One-to-Many) 관계

```kotlin
object Posts : Table("posts") {
    val id = integer("id").autoIncrement()
    val userId = reference("user_id", Users)
    val title = varchar("title", length = 200)
    val content = text("content")
    
    override val primaryKey = PrimaryKey(id)
}

class Post(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Post>(Posts)
    
    var user by User referencedOn Posts.userId
    var title by Posts.title
    var content by Posts.content
}

// User 클래스에 추가
class User(id: EntityID<Int>) : IntEntity(id) {
    // ... 기존 속성들 ...
    val posts by Post referrersOn Posts.userId
}
```

#### 3. 다대다 (Many-to-Many) 관계

```kotlin
// 이미 정의된 UserRoles 테이블 사용

class User(id: EntityID<Int>) : IntEntity(id) {
    // ... 기존 속성들 ...
    val roles by Role via UserRoles
}

class Role(id: EntityID<Int>) : IntEntity(id) {
    // ... 기존 속성들 ...
    val users by User via UserRoles
}
```

### DB Indexes & Constraints

```kotlin
object Users : Table("users") {
    // ... 기존 컬럼들 ...
    
    // 단일 컬럼 인덱스
    init {
        index(true, name) // unique = true
        index(false, createdAt) // unique = false
    }
}

object Posts : Table("posts") {
    // ... 기존 컬럼들 ...
    
    // 복합 인덱스
    init {
        index(false, userId, createdAt)
        uniqueIndex(title, content) // 복합 유니크 인덱스
    }
    
    // 외래 키 제약조건
    foreignKey(
        name = "fk_posts_user_id",
        references = Users,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )
}
```

#### Sample

```kotlin
// DSL 방식 사용
fun createUser(name: String, email: String) {
    transaction {
        Users.insert {
            it[this.name] = name
            it[this.email] = email
        }
    }
}

// DAO 방식 사용
fun createUserWithRoles(name: String, email: String, roleNames: List<String>) {
    transaction {
        val user = User.new {
            this.name = name
            this.email = email
            this.status = UserStatus.ACTIVE
        }
        
        roleNames.forEach { roleName ->
            val role = Role.find { Roles.name eq roleName }.firstOrNull()
                ?: Role.new { this.name = roleName }
            user.roles = SizedCollection(user.roles + role)
        }
    }
}
```

> **참고**: Table 객체와 Entity 클래스 선택 기준
> - **Table 객체 (DSL)**: SQL에 가까운 작업이 필요한 경우, 복잡한 쿼리가 필요한 경우
> - **Entity 클래스 (DAO)**: 객체지향적인 도메인 모델링이 필요한 경우, JPA와 유사한 방식을 선호하는 경우

---

## 5. Repository 구현

- **Spring Data JPA 스타일**
- **Exposed DSL 스타일**
- **DAO 패턴 구현**

### Spring Data JPA 스타일

```kotlin
@Repository
class UserRepository(private val database: Database) {
    
    fun save(user: UserDto): UserDto = transaction(database) {
        val entity = User.new {
            name = user.name
            email = user.email
            status = UserStatus.ACTIVE
        }
        entity.toDto()
    }
    
    fun findById(id: Int): UserDto? = transaction(database) {
        User.findById(id)?.toDto()
    }
    
    fun findAll(): List<UserDto> = transaction(database) {
        User.all().map { it.toDto() }.toList()
    }
    
    fun findByEmail(email: String): UserDto? = transaction(database) {
        User.find { Users.email eq email }.firstOrNull()?.toDto()
    }
    
    fun delete(id: Int) = transaction(database) {
        User.findById(id)?.delete()
    }
    
    private fun User.toDto() = UserDto(
        id = id.value,
        name = name,
        email = email,
        status = status
    )
}

data class UserDto(
    val id: Int? = null,
    val name: String,
    val email: String,
    val status: UserStatus = UserStatus.ACTIVE
)
```

### Exposed DSL 스타일

```kotlin
@Repository
class UserDslRepository(private val database: Database) {
    
    fun save(user: UserDto): UserDto = transaction(database) {
        val id = Users.insert {
            it[name] = user.name
            it[email] = user.email
            it[status] = UserStatus.ACTIVE
        } get Users.id
        
        user.copy(id = id)
    }
    
    fun findById(id: Int): UserDto? = transaction(database) {
        Users.select { Users.id eq id }
            .singleOrNull()
            ?.toDto()
    }
    
    fun findAll(): List<UserDto> = transaction(database) {
        Users.selectAll()
            .map { it.toDto() }
    }
    
    fun findByEmail(email: String): UserDto? = transaction(database) {
        Users.select { Users.email eq email }
            .singleOrNull()
            ?.toDto()
    }
    
    fun delete(id: Int) = transaction(database) {
        Users.deleteWhere { Users.id eq id }
    }
    
    private fun ResultRow.toDto() = UserDto(
        id = this[Users.id],
        name = this[Users.name],
        email = this[Users.email],
        status = this[Users.status]
    )
}
```

### DAO 패턴 구현

```kotlin
// 1. DAO 인터페이스 정의
interface UserDao {
    fun create(user: UserDto): UserDto
    fun read(id: Int): UserDto?
    fun update(user: UserDto): UserDto
    fun delete(id: Int)
    fun findByEmail(email: String): UserDto?
    fun findAll(): List<UserDto>
}

// 2. DAO 구현체
@Repository
class UserDaoImpl(private val database: Database) : UserDao {
    
    override fun create(user: UserDto): UserDto = transaction(database) {
        User.new {
            name = user.name
            email = user.email
            status = UserStatus.ACTIVE
        }.toDto()
    }
    
    override fun read(id: Int): UserDto? = transaction(database) {
        User.findById(id)?.toDto()
    }
    
    override fun update(user: UserDto): UserDto = transaction(database) {
        val entity = user.id?.let { User.findById(it) }
            ?: throw IllegalArgumentException("User not found")
        
        entity.apply {
            name = user.name
            email = user.email
            status = user.status
        }.toDto()
    }
    
    override fun delete(id: Int) = transaction(database) {
        User.findById(id)?.delete()
    }
    
    override fun findByEmail(email: String): UserDto? = transaction(database) {
        User.find { Users.email eq email }
            .firstOrNull()
            ?.toDto()
    }
    
    override fun findAll(): List<UserDto> = transaction(database) {
        User.all().map { it.toDto() }
    }
    
    private fun User.toDto() = UserDto(
        id = id.value,
        name = name,
        email = email,
        status = status
    )
}
```

### Dynamic Querying

```kotlin
@Repository
class UserComplexRepository(private val database: Database) {
    
    // 페이징 처리
    fun findAllWithPaging(page: Int, size: Int): List<UserDto> = transaction(database) {
        User.all()
            .limit(size, offset = ((page - 1) * size).toLong())
            .map { it.toDto() }
    }
    
    // 조인 쿼리 (DSL 방식)
    fun findUsersWithRoles(): List<UserWithRolesDto> = transaction(database) {
        (Users leftJoin UserRoles leftJoin Roles)
            .slice(Users.columns + Roles.name)
            .selectAll()
            .groupBy(
                { it[Users.id] },
                { UserRoleDto(it[Roles.id], it[Roles.name]) }
            ).map { (userId, roles) ->
                val user = Users.select { Users.id eq userId }
                    .first()
                    .let { row ->
                        UserDto(
                            id = row[Users.id],
                            name = row[Users.name],
                            email = row[Users.email],
                            status = row[Users.status]
                        )
                    }
                UserWithRolesDto(user, roles.filterNotNull())
            }
    }
    
    // 집계 함수 사용
    fun getUserStats(): UserStats = transaction(database) {
        val totalUsers = Users.selectAll().count()
        val activeUsers = Users.select { Users.status eq UserStatus.ACTIVE }.count()
        val avgPostsPerUser = Posts
            .slice(Posts.userId, Posts.id.count())
            .selectAll()
            .groupBy(Posts.userId)
            .averageBy { it[Posts.id.count()] } ?: 0.0
        
        UserStats(
            totalUsers = totalUsers,
            activeUsers = activeUsers,
            averagePostsPerUser = avgPostsPerUser
        )
    }
}

data class UserWithRolesDto(
    val user: UserDto,
    val roles: List<UserRoleDto>
)

data class UserRoleDto(
    val id: Int,
    val name: String
)

data class UserStats(
    val totalUsers: Long,
    val activeUsers: Long,
    val averagePostsPerUser: Double
)
```

### 각 구현 방식의 특징

1. **Spring Data JPA 스타일**
   - JPA 사용자에게 친숙한 패턴
   - Entity 중심의 CRUD 작업
   - 간단한 쿼리에 적합

2. **Exposed DSL 스타일**
   - SQL과 유사한 문법
   - 복잡한 쿼리 작성 용이
   - 타입 안전한 쿼리 빌더

3. **DAO 패턴**
   - 인터페이스를 통한 명확한 계약
   - 테스트 용이성
   - 구현체 교체 가능

> #### 권장 사항
> 
> - 단순 CRUD : DAO 패턴 또는 Spring Data 스타일
> - 복잡한 쿼리 : DSL 스타일
> - 대규모 프로젝트 : 인터페이스 기반 DAO 패턴
> - 작은 프로젝트 : Spring Data 스타일

---

## 6. 테스트 환경 설정

### 테스트용 데이터베이스 설정

#### H2 인메모리 데이터베이스 설정

```kotlin
@TestConfiguration
class TestDatabaseConfig {
    
    @Bean
    fun testDatabase(): Database {
        return Database.connect(
            url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )
    }
}
```

#### application-test.yml

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:
  
  # H2 콘솔 활성화 (선택사항)
  h2:
    console:
      enabled: true
      path: /h2-console
```

### 테스트 Base 클래스

```kotlin
@ActiveProfiles("test")
@SpringBootTest
abstract class ExposedTestBase {
    
    @Autowired
    protected lateinit var database: Database
    
    @BeforeEach
    fun setUp() {
        transaction(database) {
            // 테스트에 필요한 테이블 생성
            SchemaUtils.create(Users, Roles, UserRoles)
        }
    }
    
    @AfterEach
    fun tearDown() {
        transaction(database) {
            // 테스트 후 테이블 삭제
            SchemaUtils.drop(Users, Roles, UserRoles)
        }
    }
}
```

### 트랜잭션 테스트

```kotlin
@Transactional
@SpringBootTest
class UserRepositoryTest : ExposedTestBase() {
    
    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Test
    fun `사용자 생성 테스트`() {
        // given
        val userDto = UserDto(
            name = "Test User",
            email = "test@example.com"
        )
        
        // when
        val savedUser = userRepository.save(userDto)
        
        // then
        assertNotNull(savedUser.id)
        assertEquals(userDto.name, savedUser.name)
        assertEquals(userDto.email, savedUser.email)
    }
    
    @Test
    fun `사용자 조회 테스트`() {
        // given
        val user = transaction(database) {
            User.new {
                name = "Test User"
                email = "test@example.com"
                status = UserStatus.ACTIVE
            }
        }
        
        // when
        val foundUser = userRepository.findById(user.id.value)
        
        // then
        assertNotNull(foundUser)
        assertEquals(user.name, foundUser?.name)
        assertEquals(user.email, foundUser?.email)
    }
}
```

### 테스트 데이터 준비

#### 테스트 데이터 빌더

```kotlin
class TestDataBuilder(private val database: Database) {
    
    fun createUser(
        name: String = "Test User",
        email: String = "test@example.com",
        status: UserStatus = UserStatus.ACTIVE
    ): User = transaction(database) {
        User.new {
            this.name = name
            this.email = email
            this.status = status
        }
    }
    
    fun createRole(
        name: String = "TEST_ROLE"
    ): Role = transaction(database) {
        Role.new {
            this.name = name
        }
    }
    
    fun assignRoleToUser(user: User, role: Role) = transaction(database) {
        UserRoles.insert {
            it[userId] = user.id
            it[roleId] = role.id
        }
    }
}
```

#### 테스트 데이터 적용

```kotlin
@SpringBootTest
@Transactional
class UserServiceTest : ExposedTestBase() {
    
    private lateinit var testDataBuilder: TestDataBuilder
    
    @Autowired
    private lateinit var userService: UserService
    
    @BeforeEach
    override fun setUp() {
        super.setUp()
        testDataBuilder = TestDataBuilder(database)
    }
    
    @Test
    fun `사용자와 역할이 있을 때 권한 확인 테스트`() {
        // given
        val user = testDataBuilder.createUser()
        val role = testDataBuilder.createRole("ADMIN")
        testDataBuilder.assignRoleToUser(user, role)
        
        // when
        val userWithRoles = userService.findUserWithRoles(user.id.value)
        
        // then
        assertNotNull(userWithRoles)
        assertTrue(userWithRoles.roles.any { it.name == "ADMIN" })
    }
}
```

### 통합 테스트

```kotlin
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest : ExposedTestBase() {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var objectMapper: ObjectMapper
    
    private lateinit var testDataBuilder: TestDataBuilder
    
    @BeforeEach
    override fun setUp() {
        super.setUp()
        testDataBuilder = TestDataBuilder(database)
    }
    
    @Test
    fun `사용자 생성 API 테스트`() {
        // given
        val userDto = UserDto(
            name = "Test User",
            email = "test@example.com"
        )
        
        // when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
        
        // then
        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto.email))
    }
}
```

### 성능 테스트

```kotlin
@SpringBootTest
class PerformanceTest : ExposedTestBase() {
    
    @Autowired
    private lateinit var userRepository: UserRepository
    
    @Test
    fun `대량 데이터 입력 성능 테스트`() {
        val startTime = System.currentTimeMillis()
        
        transaction(database) {
            // 배치 크기 설정
            addLogger(StdOutSqlLogger)
            
            // 1만건의 데이터 입력
            repeat(10_000) { i ->
                Users.insert {
                    it[name] = "User $i"
                    it[email] = "user$i@example.com"
                    it[status] = UserStatus.ACTIVE
                }
            }
        }
        
        val endTime = System.currentTimeMillis()
        println("실행 시간: ${endTime - startTime}ms")
    }
    
    @Test
    fun `배치 처리 성능 테스트`() {
        val startTime = System.currentTimeMillis()
        
        transaction(database) {
            // 배치 처리로 1만건의 데이터 입력
            Users.batchInsert((1..10_000).toList()) { i ->
                this[Users.name] = "User $i"
                this[Users.email] = "user$i@example.com"
                this[Users.status] = UserStatus.ACTIVE
            }
        }
        
        val endTime = System.currentTimeMillis()
        println("배치 처리 실행 시간: ${endTime - startTime}ms")
    }
}
```

> #### 테스트 작성 시 주의사항
> 
> 1. 각 테스트는 독립적으로 실행될 수 있어야 함
> 2. 테스트 데이터는 테스트 시작 시 생성하고 종료 시 정리
> 3. 실제 데이터베이스 대신 인메모리 데이터베이스 사용
> 4. 트랜잭션 롤백을 활용하여 테스트 격리성 보장
> 5. 테스트 데이터 빌더를 활용하여 테스트 코드 재사용성 향상

---
