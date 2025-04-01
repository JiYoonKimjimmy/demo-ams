# Kotlin Exposed 소개

- [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
- [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
- [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
- [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
- [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
- [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
- [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

## Introduce

- `Kotlin Exposed` 는 **JetBrains** 에서 개발한 Kotlin 기반의 경량 ORM 프레임워크
- `SQL` 데이터베이스에 대한 타입 안전한 접근 제공
- `Kotlin` 특성을 최대한 활용하여 직관적이고m 간결한 데이터베이스 조작 가능

---

## Feature

### 1. **두 가지 API 스타일 제공**

#### **DSL (Domain Specific Language)**

- SQL과 유사한 문법으로 쿼리 작성
- 타입 안전한 SQL 빌더
- 복잡한 쿼리에 적합

#### **DAO (Data Access Object)**

- ORM 스타일의 엔티티 조작
- JPA와 유사한 방식
- 간단한 CRUD 작업에 적합

### 2. **Kotlin 친화적 설계**

- `Kotlin` 의 `null` 안전성 활용
- `Coroutine` 지원
- `Kotlin` 확장 함수를 통한 기능 확장 가능

### 3. **가벼운 설정**

- 최소한의 설정으로 시작 가능
- `Spring Boot Starter` 라이브러리 제공
- (H2, MySQL, PostgreSQL 등 다양한 DBMS 호환성 제공

### 프로젝트 적용 시 장점

1. **타입 안전성**
   - 컴파일 시점에 SQL 쿼리 오류 발견
   - 자동 완성 지원으로 개발 생산성 향상
2. **코드 간결성**
   - Kotlin의 특성을 활용한 간결한 문법
   - 보일러플레이트 코드 최소화
3. **유연성**
   - DSL과 DAO 스타일을 상황에 맞게 선택 가능
   - 복잡한 쿼리와 단순한 CRUD 모두 효율적으로 처리
4. **Spring Boot 통합**
   - Spring Boot Starter를 통한 쉬운 통합
   - Spring의 트랜잭션 관리와 완벽한 통합

---

## Basic Starter

### DSL 스타일

```kotlin
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val age = integer("age")
    
    override val primaryKey = PrimaryKey(id)
}

// 데이터 조회
val users = Users.select { Users.age greaterEq 18 }
    .map { row ->
        User(
            id = row[Users.id],
            name = row[Users.name],
            age = row[Users.age]
        )
    }
```

### DAO 스타일

```kotlin
class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    
    var name by Users.name
    var age by Users.age
}

// 데이터 조회
val adults = User.find { Users.age greaterEq 18 }
```

---
