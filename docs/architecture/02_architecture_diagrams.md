# 2. 아키텍처 다이어그램

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

### 2.1 고수준 아키텍처 다이어그램 (시스템 전체 구성도)

AMS 시스템의 전체 구성은 다음과 같습니다:

```mermaid
graph TB
    subgraph "Client Layer"
        Web[웹 애플리케이션]
        Mobile[모바일 앱]
    end

    subgraph "Load Balancer"
        LB[로드 밸런서]
    end

    subgraph "AMS Application"
        App[AMS 애플리케이션<br/>멀티 인스턴스 구성]
    end

    subgraph "Database Layer"
        PG[(PostgreSQL<br/>Command 처리)]
        MongoDB[(MongoDB<br/>Query 처리)]
    end

    subgraph "External Services"
        AuthService[휴대폰 인증 서비스<br/>Async + Non-Blocking]
        NotificationService[알림 발송 서비스<br/>Async + Non-Blocking]
    end

    Web --> LB
    Mobile --> LB
    LB --> App
    
    App --> PG
    App --> MongoDB
    
    App -.->|비동기 연동| AuthService
    App -.->|비동기 연동| NotificationService

    style PG fill:#336791,color:#fff
    style MongoDB fill:#13aa52,color:#fff
    style App fill:#6db33f,color:#fff
```

#### 주요 구성 요소

- **클라이언트 레이어**: 웹 애플리케이션 및 모바일 앱
- **로드 밸런서**: 다중 인스턴스 간 트래픽 분산
- **AMS 애플리케이션**: 멀티 서버 및 멀티 인스턴스로 구성되어 수평 확장을 지원합니다. 로드 밸런서를 통해 트래픽이 분산되며, 각 인스턴스는 독립적으로 운영됩니다.
- **데이터베이스 레이어**:
  - **PostgreSQL**: Command 처리용 (데이터 쓰기 작업)
  - **MongoDB**: Query 처리용 (데이터 조회 작업)
  - 모든 AMS 인스턴스는 동일한 데이터베이스에 접근합니다.
- **외부 서비스**: 비동기(Async) + Non-Blocking 방식으로 연동
  - 휴대폰 인증 서비스
  - 알림 발송 서비스
  - 각 AMS 인스턴스는 독립적으로 외부 서비스와 비동기 연동합니다.

---

### 2.2 레이어 아키텍처 다이어그램 (Hexagonal Architecture 기반)

AMS는 Hexagonal Architecture(포트/어댑터 패턴)를 기반으로 구성되며, CQRS 패턴을 적용합니다:

```mermaid
graph TB
    subgraph "Presentation Layer (Inbound Adapter)"
        Controller[REST API Controller]
    end

    subgraph "Application Layer"
        UseCase[UseCase / Application Service]
    end

    subgraph "Domain Layer (Core)"
        subgraph "Domain Model"
            Entity[Domain Entity]
            VO[Value Object]
        end
        subgraph "Inbound Port"
            InboundPort[Inbound Port Interface]
        end
        subgraph "Outbound Port"
            OutboundPort[Outbound Port Interface]
        end
        DomainService[Domain Service<br/>비즈니스 로직]
    end

    subgraph "Infrastructure Layer (Outbound Adapter)"
        PGAdapter[PostgreSQL Repository<br/>Command 처리]
        MongoAdapter[MongoDB Repository<br/>Query 처리]
        ExternalAdapter[외부 시스템 연동 어댑터<br/>휴대폰 인증, 알림 발송]
    end

    subgraph "Database"
        PG[(PostgreSQL)]
        MongoDB[(MongoDB)]
    end

    subgraph "External Systems"
        Auth[휴대폰 인증 서비스]
        Noti[알림 발송 서비스]
    end

    Controller -->|의존| InboundPort
    Controller -->|의존| UseCase
    UseCase -->|의존| InboundPort
    UseCase -->|의존| DomainService
    DomainService -->|의존| OutboundPort
    UseCase -->|의존| OutboundPort
    
    InboundPort -.->|구현| DomainService
    OutboundPort -.->|구현| PGAdapter
    OutboundPort -.->|구현| MongoAdapter
    OutboundPort -.->|구현| ExternalAdapter
    
    PGAdapter -->|Command 처리| PG
    MongoAdapter -->|Query 처리| MongoDB
    ExternalAdapter -.->|비동기 연동| Auth
    ExternalAdapter -.->|비동기 연동| Noti
    
    DomainService -->|사용| Entity
    DomainService -->|사용| VO

    style Controller fill:#4fc3f7,color:#000
    style UseCase fill:#81c784,color:#000
    style DomainService fill:#fff176,color:#000
    style PGAdapter fill:#ff9800,color:#fff
    style MongoAdapter fill:#ff9800,color:#fff
    style ExternalAdapter fill:#ff9800,color:#fff
    style PG fill:#336791,color:#fff
    style MongoDB fill:#13aa52,color:#fff
```

#### 레이어 구조 설명

1. **Presentation Layer (Inbound Adapter)**
   - REST API Controller
   - HTTP 요청/응답 처리
   - DTO 변환

2. **Application Layer**
   - UseCase / Application Service
   - 트랜잭션 경계 관리
   - 도메인 서비스 조율

3. **Domain Layer (Core - 비즈니스 로직의 핵심)**
   - **Domain Model**: Entity, Value Object
   - **Inbound Port**: 도메인이 제공하는 인터페이스
   - **Outbound Port**: 도메인이 필요로 하는 인터페이스
   - **Domain Service**: 핵심 비즈니스 로직

4. **Infrastructure Layer (Outbound Adapter)**
   - **PostgreSQL Repository**: Command 처리 (데이터 쓰기)
   - **MongoDB Repository**: Query 처리 (데이터 조회)
   - **외부 시스템 연동 어댑터**: 휴대폰 인증, 알림 발송 등

#### CQRS 패턴 적용

- **Command 처리 경로**: Controller → UseCase → Domain Service → PostgreSQL Repository → PostgreSQL
- **Query 처리 경로**: Controller → UseCase → MongoDB Repository → MongoDB
- **데이터 동기화**: Command 처리 완료 후 이벤트를 통해 MongoDB에 조회용 데이터 저장

#### 의존성 규칙

- 모든 의존성은 **Domain Layer(핵심)** 를 향합니다
- 외부 레이어는 내부 레이어를 의존하지 않습니다
- 포트(Interface)를 통해 어댑터와 도메인이 분리됩니다

---

### 2.3 컴포넌트 간 상호작용 다이어그램

#### 2.3.1 Command 처리 흐름 (데이터 생성 예시: 회원 생성)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant UseCase
    participant DomainService
    participant PGRepo as PostgreSQL<br/>Repository
    participant PostgreSQL
    participant EventHandler as Event Handler
    participant MongoRepo as MongoDB<br/>Repository
    participant MongoDB

    Client->>Controller: POST /api/v1/member
    Controller->>UseCase: createMember(dto)
    UseCase->>DomainService: validateAndCreate(member)
    DomainService->>DomainService: 비즈니스 로직 검증
    
    DomainService->>PGRepo: save(member)
    PGRepo->>PostgreSQL: INSERT
    PostgreSQL-->>PGRepo: 저장된 Entity
    PGRepo-->>DomainService: Member Entity
    
    DomainService-->>UseCase: Member Entity
    UseCase->>EventHandler: publishEvent(MemberCreated)
    
    par 비동기 처리
        EventHandler->>MongoRepo: syncToMongoDB(event)
        MongoRepo->>MongoDB: 저장 조회용 데이터
        MongoDB-->>MongoRepo: 저장 완료
    end
    
    UseCase-->>Controller: Member Entity
    Controller-->>Client: HTTP 201 Created
```

#### 2.3.2 Query 처리 흐름 (데이터 조회 예시: 회원 조회)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant UseCase
    participant MongoRepo as MongoDB<br/>Repository
    participant MongoDB

    Client->>Controller: GET /api/v1/member/{id}
    Controller->>UseCase: findMember(id)
    UseCase->>MongoRepo: findByPredicate(predicate)
    MongoRepo->>MongoDB: Query
    MongoDB-->>MongoRepo: 조회 데이터
    MongoRepo-->>UseCase: Member Entity
    UseCase-->>Controller: Member Entity
    Controller-->>Client: HTTP 200 OK
```

#### 2.3.3 Event-Driven 처리 흐름 (출석 확인 및 알림 발송)

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant UseCase
    participant DomainService
    participant PGRepo as PostgreSQL<br/>Repository
    participant PostgreSQL
    participant EventHandler as Event Handler
    participant NotificationAdapter as 알림 발송<br/>어댑터
    participant NotificationService as 알림 발송<br/>서비스

    Client->>Controller: PUT /api/v1/attendance/{id}/confirm
    Controller->>UseCase: confirmAttendance(id)
    UseCase->>DomainService: confirm(studentId, attendanceId)
    DomainService->>DomainService: 출석 확인 비즈니스 로직
    
    DomainService->>PGRepo: update(attendance)
    PGRepo->>PostgreSQL: UPDATE
    PostgreSQL-->>PGRepo: 업데이트 완료
    PGRepo-->>DomainService: Attendance Entity
    
    DomainService-->>UseCase: Attendance Entity
    UseCase->>EventHandler: publishEvent(AttendanceConfirmed)
    
    par 비동기 알림 발송 (Async + Non-Blocking)
        EventHandler->>NotificationAdapter: sendNotification(event)
        NotificationAdapter->>NotificationService: 알림 발송 요청
        Note over NotificationAdapter,NotificationService: Non-Blocking 처리
        NotificationService-->>NotificationAdapter: 발송 완료
        NotificationAdapter-->>EventHandler: 알림 발송 완료
    end
    
    UseCase-->>Controller: Attendance Entity
    Controller-->>Client: HTTP 200 OK
```

#### 상호작용 패턴 요약

1. **Command 처리**:
   - 동기적으로 PostgreSQL에 데이터 저장
   - 이벤트 발행을 통해 비동기로 MongoDB 동기화

2. **Query 처리**:
   - MongoDB에서 조회용 데이터를 직접 조회
   - 빠른 응답 시간 보장

3. **Event-Driven 처리**:
   - 도메인 이벤트를 통한 느슨한 결합
   - 비동기(Async) + Non-Blocking 방식으로 외부 시스템 연동
   - 시스템 확장성 및 성능 향상

---

