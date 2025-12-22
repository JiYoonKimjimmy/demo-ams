# 4. 아키텍처 패턴 및 원칙

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

### 4.1 아키텍처 패턴 개요

AMS는 유지보수성, 확장성, 테스트 용이성을 위해 다음과 같은 핵심 아키텍처 패턴을 적용합니다:

- **Hexagonal Architecture (포트/어댑터 패턴)**: 도메인과 외부 세계의 분리를 통한 독립성 확보
- **CQRS (Command Query Responsibility Segregation)**: 명령과 조회의 분리를 통한 성능 및 확장성 향상
- **EDD (Event-Driven Design)**: 이벤트 기반 설계를 통한 느슨한 결합 및 확장성 확보

#### 패턴 선택 이유

1. **유지보수성**: 도메인 로직과 인프라스트럭처의 분리를 통한 코드 품질 향상
2. **확장성**: 각 패턴이 독립적으로 확장 가능한 구조 제공
3. **테스트 용이성**: 포트/어댑터 패턴을 통한 Mock 객체 활용 가능
4. **성능**: CQRS 패턴을 통한 읽기/쓰기 성능 최적화
5. **유연성**: 이벤트 기반 설계를 통한 시스템 구성 요소 간 느슨한 결합

---

### 4.2 Hexagonal Architecture (포트/어댑터 패턴)

#### Hexagonal Architecture 개념

Hexagonal Architecture는 **포트/어댑터 패턴**을 기반으로 한 아키텍처 스타일로, 도메인 로직을 중심에 배치하고 외부 시스템과의 통신을 어댑터를 통해 처리합니다.

**핵심 원칙:**
- 도메인 로직은 외부 시스템에 의존하지 않습니다
- 외부 시스템은 포트(인터페이스)를 통해 도메인과 통신합니다
- 모든 의존성은 도메인을 향합니다 (의존성 역전 원칙)

#### 포트/어댑터 패턴 구성 요소

##### Inbound Port (인바운드 포트)
- **정의**: 도메인이 제공하는 인터페이스
- **역할**: 외부 시스템이 도메인 기능을 사용하기 위한 진입점
- **예시**: `MemberSaveService`, `MemberFindService` 등

##### Outbound Port (아웃바운드 포트)
- **정의**: 도메인이 필요로 하는 인터페이스
- **역할**: 도메인이 외부 시스템과 상호작용하기 위한 인터페이스
- **예시**: `MemberRepository`, `NotificationService` 등

##### Inbound Adapter (인바운드 어댑터)
- **정의**: 외부 요청을 도메인으로 변환하는 어댑터
- **역할**: HTTP 요청, 메시지 큐 등 외부 요청을 처리하여 도메인 포트로 전달
- **예시**: REST API Controller

##### Outbound Adapter (아웃바운드 어댑터)
- **정의**: 도메인 요청을 외부 시스템으로 변환하는 어댑터
- **역할**: 데이터베이스 접근, 외부 API 호출 등 도메인 요청을 외부 시스템 형식으로 변환
- **예시**: PostgreSQL Repository, MongoDB Repository, 외부 서비스 연동 어댑터

#### AMS에서의 적용 방안

##### 레이어 구성

```
┌─────────────────────────────────────────────────────────┐
│  Presentation Layer (Inbound Adapter)                   │
│  - REST API Controller                                  │
│  - HTTP 요청/응답 처리                                     │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│  Application Layer                                      │
│  - UseCase / Application Service                        │
│  - 트랜잭션 경계 관리                                       │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│  Domain Layer (Core)                                    │
│  - Inbound Port (Interface)                             │
│  - Outbound Port (Interface)                            │
│  - Domain Service (비즈니스 로직)                          │
│  - Entity, Value Object                                 │
└─────────────────────────────────────────────────────────┘
                        ↑
┌─────────────────────────────────────────────────────────┐
│  Infrastructure Layer (Outbound Adapter)                │
│  - PostgreSQL Repository                                │
│  - MongoDB Repository                                   │
│  - 외부 시스템 연동 어댑터                                   │
└─────────────────────────────────────────────────────────┘
```

##### 의존성 방향 규칙

1. **모든 의존성은 Domain Layer를 향합니다**
   - Presentation Layer → Domain Layer
   - Application Layer → Domain Layer
   - Infrastructure Layer → Domain Layer

2. **외부 레이어는 내부 레이어를 의존하지 않습니다**
   - Infrastructure Layer는 Presentation Layer를 알지 못합니다
   - Application Layer는 Infrastructure Layer의 구현 세부사항을 알지 못합니다

3. **포트(Interface)를 통해 어댑터와 도메인이 분리됩니다**
   - Inbound Port: 도메인이 제공하는 기능을 정의
   - Outbound Port: 도메인이 필요로 하는 기능을 정의

##### 도메인과 외부 세계의 분리

- **도메인 독립성**: 도메인 로직은 데이터베이스, 웹 프레임워크 등 외부 기술에 의존하지 않습니다
- **테스트 용이성**: 포트를 통해 Mock 객체를 주입하여 도메인 로직을 독립적으로 테스트할 수 있습니다
- **유연성**: 데이터베이스나 외부 시스템을 교체하더라도 도메인 로직은 변경되지 않습니다

---

### 4.3 CQRS (Command Query Responsibility Segregation)

#### CQRS 패턴 개념

CQRS는 **Command(명령)** 와 **Query(조회)** 의 책임을 분리하는 패턴입니다. 데이터 쓰기와 읽기 작업을 분리하여 각각 최적화된 구조로 구현합니다.

**핵심 원칙:**
- Command: 데이터 상태를 변경하는 작업 (CREATE, UPDATE, DELETE)
- Query: 데이터를 조회하는 작업 (READ)
- Command와 Query를 별도의 모델과 저장소로 분리

#### Command/Query 분리 전략

##### Command 처리 (데이터 쓰기)

- **데이터 소스**: PostgreSQL (RDBMS)
- **처리 작업**: 
  - CREATE: 데이터 생성
  - UPDATE: 데이터 수정
  - DELETE: 데이터 삭제
- **특징**:
  - ACID 트랜잭션 보장
  - 데이터 무결성 유지
  - 복잡한 관계형 데이터 모델링

##### Query 처리 (데이터 조회)

- **데이터 소스**: MongoDB (NoSQL)
- **처리 작업**: 
  - READ: 데이터 조회
  - 검색 및 집계 쿼리
- **특징**:
  - 빠른 조회 성능
  - 조회 최적화된 데이터 구조
  - 수평 확장 용이

##### 데이터 소스 분리 이유

| 항목 | PostgreSQL (Command) | MongoDB (Query) |
|------|---------------------|-----------------|
| **용도** | 데이터 쓰기 작업 | 데이터 조회 작업 |
| **최적화** | ACID 트랜잭션, 데이터 무결성 | 빠른 조회 성능, 수평 확장 |
| **스키마** | 정규화된 관계형 스키마 | 조회 최적화된 문서 스키마 |
| **장점** | 데이터 일관성 보장 | 높은 조회 성능 |

#### CQRS 구현 방식

##### Command 처리 경로

```
Client → Controller → UseCase → Domain Service → PostgreSQL Repository → PostgreSQL
```

**처리 단계:**
1. **Controller**: HTTP 요청을 받아 DTO로 변환
2. **UseCase**: 트랜잭션 경계 관리 및 도메인 서비스 조율
3. **Domain Service**: 비즈니스 로직 검증 및 처리
4. **PostgreSQL Repository**: Kotlin Exposed를 통한 타입 안전한 쿼리 작성
5. **PostgreSQL**: 트랜잭션 보장된 데이터 저장

##### Query 처리 경로

```
Client → Controller → UseCase → MongoDB Repository → MongoDB
```

**처리 단계:**
1. **Controller**: HTTP 요청을 받아 DTO로 변환
2. **UseCase**: 조회 요청 처리 로직
3. **MongoDB Repository**: MongoDB Kotlin Driver를 통한 Coroutine 기반 비동기 쿼리
4. **MongoDB**: 조회 최적화된 데이터 구조에서 빠른 조회

##### 데이터 동기화 전략

Command 처리 완료 후 이벤트를 발행하여 MongoDB에 조회용 데이터를 비동기로 동기화합니다:

```
PostgreSQL 저장 완료 
  → 도메인 이벤트 발행 (MemberCreated, MemberUpdated 등)
  → 이벤트 핸들러가 비동기로 MongoDB에 조회용 데이터 저장
```

**동기화 특징:**
- **비동기 처리**: Command 응답 시간에 영향 없음
- **이벤트 기반**: 느슨한 결합 유지
- **최종 일관성**: Eventual Consistency 모델

#### Event Sourcing 적용 여부

**Event Sourcing 미적용**

AMS는 **단순 CQRS 패턴**만 적용하며, Event Sourcing은 사용하지 않습니다.

**이유:**
1. **복잡도 관리**: Event Sourcing은 시스템 복잡도를 높일 수 있습니다
2. **요구사항**: 현재 비즈니스 요구사항에 Event Sourcing이 필수적이지 않습니다
3. **충분한 이점**: CQRS 패턴만으로도 읽기/쓰기 성능 최적화가 가능합니다

**대안:**
- **도메인 이벤트**: Command 처리 완료 후 도메인 이벤트를 발행하여 데이터 동기화
- **이벤트 기반 동기화**: Event-Driven Design을 통해 비동기 데이터 동기화

**결론:**
- Event Sourcing은 적용하지 않음
- 도메인 이벤트를 통한 데이터 동기화만 사용
- 필요 시 향후 Event Sourcing 도입 가능한 구조로 설계

---

### 4.4 EDD (Event-Driven Design)

#### EDD 개념 및 목적

EDD(Event-Driven Design)는 도메인 이벤트를 중심으로 시스템을 설계하는 아키텍처 스타일입니다.

**핵심 원칙:**
- 도메인 이벤트를 통한 시스템 구성 요소 간 통신
- 이벤트 발행자와 수신자의 느슨한 결합
- 비동기 이벤트 처리로 시스템 확장성 향상

**적용 목적:**
- 시스템 구성 요소 간 느슨한 결합 확보
- 비동기 처리를 통한 성능 향상
- 이벤트 기반 확장 가능한 구조 제공

#### 도메인 이벤트 설계

##### 이벤트 종류

AMS에서 발행하는 주요 도메인 이벤트:

| 이벤트 이름 | 설명 | 발행 시점 |
|-----------|------|----------|
| `MemberCreated` | 회원 정보 생성 완료 | 회원 생성 Command 처리 완료 후 |
| `MemberUpdated` | 회원 정보 수정 완료 | 회원 수정 Command 처리 완료 후 |
| `AttendanceConfirmed` | 출석 확인 완료 | 출석 확인 Command 처리 완료 후 |
| `ClassCreated` | 수업 생성 완료 | 수업 생성 Command 처리 완료 후 |
| `ClassUpdated` | 수업 정보 수정 완료 | 수업 수정 Command 처리 완료 후 |

##### 이벤트 발행 시점

AMS는 **트랜잭션 커밋 완료 후 비동기로 이벤트를 발행**합니다.

**발행 전략:**
- **트랜잭션 커밋 후 발행**: PostgreSQL 트랜잭션이 성공적으로 커밋된 후에 이벤트 발행
- **비동기 처리**: 이벤트 발행과 처리를 비동기로 수행하여 Command 응답 시간에 영향 없음

이 방식은 트랜잭션이 롤백된 경우 이벤트가 발행되지 않아 데이터 일관성을 보장합니다.

##### 이벤트 핸들러

이벤트 핸들러는 발행된 이벤트를 수신하여 후속 작업을 처리합니다:

| 이벤트 핸들러 | 처리 작업 | 목적 |
|------------|---------|------|
| `MongoDBSyncHandler` | MongoDB에 조회용 데이터 저장 | CQRS 데이터 동기화 |
| `NotificationHandler` | 학부모에게 알림 발송 | 출석 확인 알림 등 |
| `StatisticsHandler` | 통계 데이터 업데이트 | 운영 통계 계산 |

#### 이벤트 처리 방식

##### 비동기 이벤트 처리 (Non-Blocking)

- **이벤트 발행**: Command 처리 완료 후 이벤트를 발행하고 즉시 응답 반환
- **비동기 처리**: 이벤트 핸들러가 별도의 스레드에서 비동기로 처리
- **Non-Blocking**: 이벤트 처리가 Command 응답 시간에 영향을 주지 않음

##### 이벤트 기반 느슨한 결합

- **이벤트 발행자와 수신자 분리**: 이벤트 발행자는 수신자의 존재를 알 필요 없음
- **독립적인 확장**: 이벤트 핸들러는 독립적으로 추가/제거 가능
- **유연한 통신**: 이벤트를 통해 시스템 구성 요소 간 통신

##### 시스템 확장성 향상

- **수평 확장**: 이벤트 핸들러를 여러 인스턴스로 확장 가능
- **부하 분산**: 이벤트 기반 메시지 큐를 통한 부하 분산
- **결합도 감소**: 직접적인 의존성 없이 이벤트를 통한 통신

---

### 4.5 도메인 모델 설계

#### 도메인 중심 설계 (Domain-Driven Design) 원칙

AMS는 Domain-Driven Design(DDD) 원칙을 따라 도메인 모델을 설계합니다.

**핵심 원칙:**
1. **도메인 모델 중심**: 비즈니스 로직을 도메인 모델에 집중
2. **유비쿼터스 언어**: 비즈니스 도메인의 용어를 코드에서도 사용
3. **경계 컨텍스트**: 도메인별로 명확한 경계 설정
4. **도메인 서비스**: 여러 엔티티에 걸친 비즈니스 로직 처리

#### 도메인 모델 구성

##### Entity (엔티티)

- **정의**: 식별자를 가진 도메인 객체
- **특징**: 
  - 고유한 식별자(ID)를 가짐
  - 생명주기를 가짐
  - 상태 변경 가능
- **예시**: `Student`, `Academy`, `Class`, `Teacher` 등

##### Value Object (값 객체)

- **정의**: 값으로 정의되는 불변 객체
- **특징**:
  - 식별자 없음
  - 값이 같으면 동일한 객체
  - 불변성(Immutable)
- **예시**: `Address`, `Money`, `Email` 등

##### Domain Service (도메인 서비스)

- **정의**: 여러 엔티티에 걸친 비즈니스 로직을 처리하는 서비스
- **특징**:
  - 엔티티나 값 객체에 속하지 않는 로직 처리
  - 상태 없이 순수한 비즈니스 로직만 포함
- **예시**: `MemberSaveService`, `MemberFindService` 등

##### Repository Interface (레포지토리 인터페이스)

- **정의**: 도메인이 필요로 하는 데이터 접근 인터페이스
- **특징**:
  - 도메인 레이어에 정의 (Outbound Port)
  - 구현은 Infrastructure Layer에서 담당
- **예시**: `MemberRepository`, `AcademyRepository` 등

#### 도메인 모델 예시

##### Student (학생 엔티티)

```kotlin
// 도메인 모델 예시 (의사 코드)
class Student(
    val id: StudentId,              // 식별자
    val name: String,                // 이름
    val mobileNumber: PhoneNumber,   // Value Object
    val dateOfBirth: DateOfBirth,    // Value Object
    val address: Address,            // Value Object
    val school: School,              // Value Object
    val grade: Grade,               // Value Object
    val status: ActivationStatus     // 상태
) {
    // 도메인 로직
    fun isActive(): Boolean {
        return status == ActivationStatus.ACTIVE
    }
}
```

##### 주요 도메인 모델

AMS의 주요 도메인 모델:

- **학원 정보 관리 도메인**: `Academy`, `AcademyInfo`
- **회원 관리 도메인**: `Student`, `Parent`, `Teacher`, `Manager`
- **수업 관리 도메인**: `Class`, `ClassSchedule`, `Attendance`, `Evaluation`
- **운영 관리 도메인**: `Payment`, `Receipt`, `Statistics`

---

### 4.6 아키텍처 패턴 통합

#### Hexagonal Architecture + CQRS + EDD 통합

AMS는 세 가지 아키텍처 패턴을 통합하여 강력하고 확장 가능한 시스템을 구축합니다:

```
┌─────────────────────────────────────────────────────────────┐
│ Hexagonal Architecture                                      │
│  - 도메인 중심 설계                                            │
│  - 포트/어댑터 패턴                                            │
│  - 의존성 역전 원칙                                            │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ CQRS                                                        │
│  - Command: PostgreSQL (쓰기)                                │
│  - Query: MongoDB (조회)                                     │
│  - 데이터 소스 분리                                            │
└─────────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────────┐
│ EDD                                                         │
│  - 도메인 이벤트 발행                                           │
│  - 비동기 이벤트 처리                                           │
│  - 데이터 동기화                                               │
└─────────────────────────────────────────────────────────────┘
```

#### 각 패턴 간 상호작용

1. **Hexagonal Architecture + CQRS**
   - Hexagonal Architecture의 Outbound Adapter가 CQRS의 데이터 소스를 분리
   - PostgreSQL Repository와 MongoDB Repository를 별도의 어댑터로 구현
   - 도메인은 포트(인터페이스)를 통해 각 Repository에 접근

2. **CQRS + EDD**
   - Command 처리 완료 후 도메인 이벤트 발행
   - 이벤트 핸들러가 MongoDB에 조회용 데이터 동기화
   - 비동기 이벤트 처리를 통한 데이터 동기화

3. **Hexagonal Architecture + EDD**
   - 이벤트 발행 및 처리를 Outbound Adapter에서 담당
   - 도메인은 이벤트 인터페이스(포트)만 정의
   - Infrastructure Layer에서 실제 이벤트 처리 구현

#### 전체 아키텍처 구조 요약

AMS의 아키텍처는 다음과 같이 구성됩니다:

1. **레이어 구조**: Hexagonal Architecture 기반의 4계층 구조
   - Presentation Layer (Inbound Adapter)
   - Application Layer
   - Domain Layer (Core)
   - Infrastructure Layer (Outbound Adapter)

2. **데이터 분리**: CQRS 패턴을 통한 Command/Query 분리
   - Command: PostgreSQL (트랜잭션 보장)
   - Query: MongoDB (조회 성능 최적화)

3. **이벤트 처리**: EDD를 통한 비동기 이벤트 처리
   - 도메인 이벤트 발행
   - 비동기 데이터 동기화
   - 느슨한 결합 구조

4. **통합 효과**:
   - **유지보수성**: 명확한 레이어 분리로 코드 품질 향상
   - **확장성**: 독립적으로 확장 가능한 구조
   - **성능**: 읽기/쓰기 성능 최적화
   - **유연성**: 각 구성 요소 간 느슨한 결합

---

## 다음 문서

[→ 5. 데이터 아키텍처](05_data_architecture.md)

