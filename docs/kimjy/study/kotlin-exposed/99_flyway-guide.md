# Flyway

## Introduce

### Flyway 데이터베이스 마이그레이션 도구

- `Flyway` 는 데이터베이스 스키마 버전 관리 도구로서, 데이터베이스의 변경 사항을 코드로 관리할 수 있도록 지원
- 특히 `Exposed` 와 함께 사용할 때, `JPA` 자동 스키마 생성 기능을 대체할 수 있는 안정적인 방법 제공

## Agenda

1. [설정 방법](#1-설정-방법)
2. [마이그레이션 파일 작성](#2-마이그레이션-파일-작성)
3. [작동 방식](#3-작동-방식)
4. [Exposed와 함께 사용하기](#4-exposed와-함께-사용하기)
5. [모범 사례와 주의사항](#5-모범-사례와-주의사항)

## 1. 설정 방법

### 의존성 추가

```kotlin
dependencies {
    implementation("org.flywaydb:flyway-core")
    // MySQL 8.0 이상을 사용하는 경우
    implementation("org.flywaydb:flyway-mysql")
}
```

### application.yml 설정

```yaml
spring:
  flyway:
    enabled: true                      # Flyway 활성화
    locations: classpath:db/migration  # 마이그레이션 스크립트 위치
    baseline-on-migrate: true          # 기존 DB에 Flyway 적용 시 필요
    baseline-version: 0                # 기준 버전
    validate-on-migrate: true          # 마이그레이션 전 유효성 검사
    clean-disabled: true               # DB 초기화 비활성화 (운영 환경 보호)
```

---

## 2. 마이그레이션 파일 작성

### 파일 생성 규칙

- 데이터베이스 마이그레이션 파일은 **`V{버전}__{설명}.sql` 형식으로 작성**

### Sample

```
src/main/resources/
└── db
    └── migration
        ├── V1__create_users_table.sql
        ├── V2__create_roles_table.sql
        └── V3__add_email_to_users.sql
```

```sql
-- V1__create_users_table.sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- V2__create_roles_table.sql
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- V3__add_email_to_users.sql
ALTER TABLE users
ADD COLUMN email VARCHAR(255);
```

## 3. 작동 방식

### 버전 관리

- 각 마이그레이션 스크립트는 순차적으로 실행
- `flyway_schema_history` 테이블에 실행 이력 기록
- 한 번 적용된 스크립트는 수정 불가

### 반복 가능한 마이그레이션

- `R__` 접두사를 사용하여 매번 실행되는 스크립트 작성 가능
- 예: `R__update_view.sql`

### 검증 프로세스

- 이미 적용된 마이그레이션 스크립트의 체크섬 확인
- 스크립트 변경 여부 감지
- 변경된 경우 에러 발생

## 4. Exposed 와 함께 사용하기

```kotlin
@Configuration
class DatabaseConfig {

    @Bean
    fun databaseSetup(database: Database) = transaction(database) {
        // `Flyway` 가 생성한 테이블과 매핑되는 `Exposed` 테이블 객체 정의
        object Users : Table("users") {
            val id = integer("id").autoIncrement()
            val name = varchar("name", 100)
            val email = varchar("email", 255).nullable()
            val createdAt = datetime("created_at")
            
            override val primaryKey = PrimaryKey(id)
        }
        
        // 추가 테이블 정의...
    }
    
}
```

## 5. 모범 사례와 주의사항

### 장점
- 데이터베이스 스키마 변경 이력 추적 가능
- 팀 협업 시 스키마 동기화 용이
- 운영 환경 마이그레이션 안정성 확보
- 롤백 및 복구 전략 수립 가능

### 주의사항
- 운영 환경에서는 `clean-disabled: true` 설정 필수
- 마이그레이션 스크립트는 한 번 적용 후 수정 금지
- 버전 번호는 순차적으로 관리
- 스크립트 작성 시 멱등성 고려

### 권장 사항
1. 각 마이그레이션 파일은 한 가지 목적만 수행
2. 스크립트 이름은 명확하고 설명적으로 작성
3. 주석을 통해 변경 사항 문서화
4. 운영 환경 적용 전 테스트 환경에서 검증

---
