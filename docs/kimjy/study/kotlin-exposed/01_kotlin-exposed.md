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

> **참고**: `exposed-spring-boot-starter`와 `exposed-spring-transaction`의 차이
> - `exposed-spring-transaction`: Spring의 트랜잭션 관리 기능만 제공
> - `exposed-spring-boot-starter`: 자동 설정을 포함한 모든 필요한 의존성 포함
> 
> 일반적으로는 더 세밀한 제어가 가능한 `exposed-spring-transaction`을 사용하는 것을 권장합니다.
> 단, Spring Boot의 자동 설정 기능을 선호하는 경우에는 `exposed-spring-boot-starter`를 사용할 수 있습니다.
