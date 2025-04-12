# Exposed DSL 심화 학습

> - [0. 📚 Exposed 소개](./00_kotlin-exposed.md)
> - [1. ⚙️ Exposed with Spring Boot 설정](./01_kotlin-exposed.md)
> - [2. 🔧 Exposed DSL 심화 학습](./02_kotlin-exposed.md)
> - [3. 🏭 Exposed DAO 패턴 활용](./03_kotlin-exposed.md)
> - [4. 💫 트랜잭션 관리](./04_kotlin-exposed.md)
> - [5. 🔍 복잡한 쿼리 작성](./05_kotlin-exposed.md)
> - [6. ⚡ 성능 최적화](./06_kotlin-exposed.md)

---

## Agenda

- [Exposed DSL 심화 학습](#exposed-dsl-심화-학습)
  - [Agenda](#agenda)
  - [1. DSL 기본 문법](#1-dsl-기본-문법)
    - [SELECT 쿼리](#select-쿼리)
    - [INSERT 쿼리](#insert-쿼리)
    - [UPDATE 쿼리](#update-쿼리)
    - [DELETE 쿼리](#delete-쿼리)
  - [2. 조건문과 연산자](#2-조건문과-연산자)
    - [비교 연산자](#비교-연산자)
    - [논리 연산자](#논리-연산자)
    - [IN 연산자](#in-연산자)
    - [LIKE 연산자](#like-연산자)
  - [3. 조인과 서브쿼리](#3-조인과-서브쿼리)
    - [INNER JOIN](#inner-join)
    - [LEFT JOIN](#left-join)
    - [RIGHT JOIN](#right-join)
    - [서브쿼리](#서브쿼리)
  - [4. 집계 함수와 그룹화](#4-집계-함수와-그룹화)
    - [COUNT, SUM, AVG](#count-sum-avg)
    - [GROUP BY](#group-by)
    - [HAVING](#having)
    - [WINDOW 함수](#window-함수)
  - [5. 정렬과 페이징](#5-정렬과-페이징)
    - [ORDER BY](#order-by)
    - [LIMIT, OFFSET](#limit-offset)
  - [6. 배치 처리](#6-배치-처리)
    - [배치 INSERT](#배치-insert)
    - [배치 UPDATE](#배치-update)
    - [배치 DELETE](#배치-delete)

---

## 1. DSL 기본 문법

### SELECT 쿼리

```kotlin
// 기본 SELECT
val users = Users.selectAll()

// 특정 컬럼 선택
val userNames = Users.slice(Users.name, Users.email).selectAll()

// 조건부 SELECT
val activeUsers = Users.select { Users.status eq UserStatus.ACTIVE }

// 결과 매핑
val userDtos = Users.selectAll().map { row ->
    UserDto(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email]
    )
}
```

### INSERT 쿼리

```kotlin
// 단일 INSERT
val userId = Users.insert {
    it[name] = "John Doe"
    it[email] = "john@example.com"
    it[status] = UserStatus.ACTIVE
} get Users.id

// 배치 INSERT
Users.batchInsert(userList) { user ->
    this[Users.name] = user.name
    this[Users.email] = user.email
    this[Users.status] = UserStatus.ACTIVE
}
```

### UPDATE 쿼리

```kotlin
// 단일 레코드 업데이트
Users.update({ Users.id eq userId }) {
    it[name] = "Updated Name"
    it[email] = "updated@example.com"
}

// 조건부 업데이트
Users.update({ Users.status eq UserStatus.INACTIVE }) {
    it[status] = UserStatus.ACTIVE
}
```

### DELETE 쿼리

```kotlin
// 단일 레코드 삭제
Users.deleteWhere { Users.id eq userId }

// 조건부 삭제
Users.deleteWhere { Users.status eq UserStatus.INACTIVE }
```

## 2. 조건문과 연산자

### 비교 연산자

```kotlin
// 동등 비교
Users.select { Users.name eq "John" }

// 크기 비교
Users.select { Users.age greater 18 }
Users.select { Users.age less 65 }
Users.select { Users.age greaterEq 18 }
Users.select { Users.age lessEq 65 }

// NULL 체크
Users.select { Users.email.isNull() }
Users.select { Users.email.isNotNull() }
```

### 논리 연산자

```kotlin
// AND
Users.select { 
    (Users.age greaterEq 18) and 
    (Users.status eq UserStatus.ACTIVE) 
}

// OR
Users.select { 
    (Users.status eq UserStatus.ACTIVE) or 
    (Users.status eq UserStatus.INACTIVE) 
}

// NOT
Users.select { Users.status neq UserStatus.ACTIVE }
```

### IN 연산자

```kotlin
// 값 목록
Users.select { Users.status inList listOf(UserStatus.ACTIVE, UserStatus.INACTIVE) }

// 서브쿼리
Users.select { Users.id inSubQuery Posts.slice(Posts.userId).selectAll() }
```

### LIKE 연산자

```kotlin
// 시작 문자열
Users.select { Users.name like "John%" }

// 포함 문자열
Users.select { Users.email like "%@gmail.com" }

// 정규식
Users.select { Users.email regexp "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$" }
```

## 3. 조인과 서브쿼리

### INNER JOIN

```kotlin
// 기본 INNER JOIN
(Users innerJoin Posts)
    .select { Users.id eq Posts.userId }
    .map { row ->
        UserPostDto(
            userId = row[Users.id],
            userName = row[Users.name],
            postId = row[Posts.id],
            postTitle = row[Posts.title]
        )
    }

// 별칭 사용
val u = Users.alias("u")
val p = Posts.alias("p")
(u innerJoin p)
    .select { u[Users.id] eq p[Posts.userId] }
```

### LEFT JOIN

```kotlin
// LEFT JOIN
(Users leftJoin Posts)
    .select { Users.id eq Posts.userId }
    .map { row ->
        UserPostDto(
            userId = row[Users.id],
            userName = row[Users.name],
            postId = row[Posts.id]?.value,
            postTitle = row[Posts.title]?.value
        )
    }
```

### RIGHT JOIN

```kotlin
// RIGHT JOIN
(Users rightJoin Posts)
    .select { Users.id eq Posts.userId }
    .map { row ->
        UserPostDto(
            userId = row[Users.id]?.value,
            userName = row[Users.name]?.value,
            postId = row[Posts.id],
            postTitle = row[Posts.title]
        )
    }
```

### 서브쿼리

```kotlin
// 스칼라 서브쿼리
val postCount = Posts.slice(Posts.userId, Posts.id.count())
    .selectAll()
    .groupBy(Posts.userId)

Users.select { 
    Users.id inSubQuery postCount.slice(postCount[Posts.userId])
}

// EXISTS
Users.select { 
    exists(
        Posts.select { 
            (Posts.userId eq Users.id) and 
            (Posts.createdAt greater LocalDateTime.now().minusDays(7))
        }
    )
}
```

## 4. 집계 함수와 그룹화

### COUNT, SUM, AVG

```kotlin
// COUNT
val totalUsers = Users.selectAll().count()

// SUM
val totalAge = Users.slice(Users.age.sum()).selectAll().first()[Users.age.sum()]

// AVG
val averageAge = Users.slice(Users.age.avg()).selectAll().first()[Users.age.avg()]
```

### GROUP BY

```kotlin
// 단일 컬럼 그룹화
Users.slice(Users.status, Users.id.count())
    .selectAll()
    .groupBy(Users.status)
    .map { row ->
        StatusCount(
            status = row[Users.status],
            count = row[Users.id.count()]
        )
    }

// 복합 컬럼 그룹화
Users.slice(Users.status, Users.age, Users.id.count())
    .selectAll()
    .groupBy(Users.status, Users.age)
```

### HAVING

```kotlin
// HAVING 절
Users.slice(Users.status, Users.id.count())
    .selectAll()
    .groupBy(Users.status)
    .having { Users.id.count() greater 10 }
```

### WINDOW 함수

```kotlin
// ROW_NUMBER
Users.slice(
    Users.id,
    Users.name,
    rowNumber().over { orderBy(Users.createdAt) }
).selectAll()

// RANK
Users.slice(
    Users.id,
    Users.name,
    rank().over { 
        partitionBy(Users.status)
        orderBy(Users.createdAt)
    }
).selectAll()
```

## 5. 정렬과 페이징

### ORDER BY

```kotlin
// 단일 컬럼 정렬
Users.selectAll()
    .orderBy(Users.name)

// 복합 컬럼 정렬
Users.selectAll()
    .orderBy(Users.status, Users.createdAt to SortOrder.DESC)

// NULLS FIRST/LAST
Users.selectAll()
    .orderBy(Users.email to SortOrder.ASC, nullsFirst = true)
```

### LIMIT, OFFSET

```kotlin
// 기본 페이징
Users.selectAll()
    .limit(10, offset = 20)

// 페이징 처리
fun getUsers(page: Int, size: Int): List<UserDto> {
    return Users.selectAll()
        .limit(size, offset = ((page - 1) * size).toLong())
        .map { row ->
            UserDto(
                id = row[Users.id],
                name = row[Users.name],
                email = row[Users.email]
            )
        }
}
```

## 6. 배치 처리

### 배치 INSERT

```kotlin
// 배치 INSERT
Users.batchInsert(userList) { user ->
    this[Users.name] = user.name
    this[Users.email] = user.email
    this[Users.status] = UserStatus.ACTIVE
}

// 배치 크기 지정
Users.batchInsert(userList, batchSize = 100) { user ->
    this[Users.name] = user.name
    this[Users.email] = user.email
}
```

> **일반 INSERT vs 배치 INSERT 차이점**
> 
> 1. **성능 차이**
>   - 일반 INSERT: 각 레코드마다 개별 SQL 문 실행
>   - 배치 INSERT: 여러 레코드를 하나의 SQL 문으로 처리
>   - 대량 데이터 처리 시 배치 INSERT가 훨씬 빠름
> 
> 2. **트랜잭션 처리**
>   - 일반 INSERT: 각 INSERT가 개별 트랜잭션으로 처리
>   - 배치 INSERT: 지정된 배치 크기만큼의 레코드를 하나의 트랜잭션으로 처리
> 
> 3. **메모리 사용**
>   - 일반 INSERT: 메모리 사용량이 적음
>   - 배치 INSERT: 배치 크기에 따라 메모리 사용량 증가
> 
> 4. **에러 처리**
>   - 일반 INSERT: 개별 레코드 실패 시 해당 레코드만 롤백
>   - 배치 INSERT: 배치 단위로 롤백 처리
> 
> 5. **사용 시나리오**
>   - 일반 INSERT: 단일 레코드 또는 소량 데이터 처리
>   - 배치 INSERT: 대량 데이터 처리, 초기 데이터 로딩, 데이터 마이그레이션
> 
> 6. **배치 크기 설정**
>   - 너무 작은 배치 크기: 성능 향상 효과 미미
>   - 너무 큰 배치 크기: 메모리 부족 가능성
>   - 권장 배치 크기: 100~1000 사이 (데이터베이스와 상황에 따라 조정)

> **MySQL 실제 실행 쿼리 예시**
> 
> 1. **일반 INSERT 실행 쿼리**
> ```sql
> -- 일반 INSERT (각 레코드마다 개별 실행)
> INSERT INTO users (name, email, status) VALUES ('John Doe', 'john@example.com', 'ACTIVE');
> INSERT INTO users (name, email, status) VALUES ('Jane Smith', 'jane@example.com', 'ACTIVE');
> INSERT INTO users (name, email, status) VALUES ('Bob Johnson', 'bob@example.com', 'ACTIVE');
> ```
> 
> 2. **배치 INSERT 실행 쿼리**
> ```sql
> -- 배치 INSERT (하나의 쿼리로 여러 레코드 처리)
> INSERT INTO users (name, email, status) 
> VALUES 
>   ('John Doe', 'john@example.com', 'ACTIVE'),
>   ('Jane Smith', 'jane@example.com', 'ACTIVE'),
>   ('Bob Johnson', 'bob@example.com', 'ACTIVE');
> ```
> 
> 3. **배치 크기가 2인 경우의 실행 쿼리**
> ```sql
> -- 첫 번째 배치
> INSERT INTO users (name, email, status) 
> VALUES 
>   ('John Doe', 'john@example.com', 'ACTIVE'),
>   ('Jane Smith', 'jane@example.com', 'ACTIVE');
> 
> -- 두 번째 배치
> INSERT INTO users (name, email, status) 
> VALUES 
>   ('Bob Johnson', 'bob@example.com', 'ACTIVE');
> ```
> 
> 4. **성능 비교**
> ```sql
> -- 일반 INSERT (3개의 쿼리 실행)
> -- 실행 시간: ~3ms (각 쿼리당 ~1ms)
> 
> -- 배치 INSERT (1개의 쿼리 실행)
> -- 실행 시간: ~1ms
> 
> -- 배치 크기 2 (2개의 쿼리 실행)
> -- 실행 시간: ~2ms
> ```
> 
> 5. **트랜잭션 처리 예시**
> ```sql
> -- 일반 INSERT (각각 독립적인 트랜잭션)
> BEGIN;
> INSERT INTO users (name, email, status) VALUES ('John Doe', 'john@example.com', 'ACTIVE');
> COMMIT;
> 
> BEGIN;
> INSERT INTO users (name, email, status) VALUES ('Jane Smith', 'jane@example.com', 'ACTIVE');
> COMMIT;
> 
> -- 배치 INSERT (하나의 트랜잭션)
> BEGIN;
> INSERT INTO users (name, email, status) 
> VALUES 
>   ('John Doe', 'john@example.com', 'ACTIVE'),
>   ('Jane Smith', 'jane@example.com', 'ACTIVE');
> COMMIT;
> ```

### 배치 UPDATE

```kotlin
// 배치 UPDATE
Users.update({ Users.status eq UserStatus.INACTIVE }) {
    it[status] = UserStatus.ACTIVE
    it[updatedAt] = LocalDateTime.now()
}

// 조건부 배치 UPDATE
Users.update({ Users.id inList userIds }) {
    it[status] = UserStatus.ACTIVE
}
```

### 배치 DELETE

```kotlin
// 배치 DELETE
Users.deleteWhere { Users.id inList userIds }

// 조건부 배치 DELETE
Users.deleteWhere { 
    (Users.status eq UserStatus.INACTIVE) and 
    (Users.lastLoginAt less LocalDateTime.now().minusMonths(6))
}
```

> **참고**: 배치 처리 시 주의사항
> 
> 1. 적절한 배치 크기 설정
>   - 너무 작으면 성능 저하
>   - 너무 크면 메모리 부족 가능성
> 
> 2. 트랜잭션 관리
>   - 대량의 데이터 처리 시 트랜잭션 분할 고려
> 
> 3. 에러 처리
>   - 배치 처리 중 에러 발생 시 롤백 전략 수립

---
