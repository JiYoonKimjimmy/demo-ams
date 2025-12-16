# 7. 비동기 처리 전략

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

### 7.1 Coroutine 사용 영역 및 사용 이유

#### Coroutine 선택 이유

AMS는 **Kotlin Coroutine**을 비동기 처리의 핵심 기술로 사용합니다.

**주요 선택 이유:**
- **경량 스레드**: 기존 스레드보다 훨씬 가벼운 동시성 처리로 리소스 효율성 향상
- **Non-Blocking I/O**: I/O 대기 중에도 다른 작업을 처리하여 높은 처리량 확보
- **Structured Concurrency**: 안전한 동시성 처리 보장 및 리소스 누수 방지
- **직관적인 코드**: `suspend` 함수를 통한 동기 코드처럼 보이는 비동기 코드 작성

#### Coroutine 사용 영역

**1. 비즈니스 로직 처리**
- **UseCase 레이어**: `suspend` 함수를 통한 비동기 비즈니스 로직 처리
- **Domain Service**: 도메인 로직의 비동기 실행
- **트랜잭션 관리**: Exposed의 `newSuspendedTransaction`을 통한 비동기 트랜잭션 처리

**2. 데이터베이스 접근**
- **PostgreSQL**: Exposed의 `newSuspendedTransaction` 및 커스텀 `tx` 함수를 통한 Non-Blocking 트랜잭션 처리
- **MongoDB**: MongoDB Kotlin Driver의 `suspend` 함수를 통한 Non-Blocking 쿼리 처리
- **동시성**: 여러 데이터베이스 작업을 동시에 처리 가능

**3. 이벤트 핸들러 처리**
- **이벤트 핸들러**: 도메인 이벤트를 비동기로 처리
- **MongoDB 동기화**: PostgreSQL 데이터를 MongoDB로 비동기 동기화
- **알림 발송**: 외부 알림 서비스 호출을 비동기로 처리

#### Coroutine 주요 특징

**경량 스레드**
- 기존 스레드와 달리 수천 개의 코루틴을 생성해도 오버헤드가 적음
- 스레드 풀을 효율적으로 활용하여 높은 동시성 처리 가능

**Non-Blocking I/O**
- I/O 작업 대기 중에도 다른 코루틴이 실행되어 스레드 블로킹 방지
- 단일 스레드에서도 여러 작업을 동시에 처리 가능

**Suspend 함수**
- 비동기 코드를 동기 코드처럼 작성하여 가독성 향상
- 예외 처리와 제어 흐름이 직관적

**Structured Concurrency**
- 코루틴 스코프를 통해 안전한 동시성 처리 보장
- 부모 코루틴이 취소되면 자식 코루틴도 자동 취소되어 리소스 누수 방지

### 7.2 Application 내부 Thread 처리 방식

#### Coroutine 기반 Non-Blocking 처리

AMS는 **Coroutine 기반 Non-Blocking 처리**를 통해 Application 내부의 모든 작업을 처리합니다.

**처리 방식:**
- 모든 비즈니스 로직과 데이터베이스 접근을 `suspend` 함수로 구현
- 스레드 블로킹 없이 I/O 작업 처리
- 높은 동시성과 처리량 확보

#### Dispatcher 활용

**Dispatchers.IO**
- **사용 목적**: I/O 작업(데이터베이스 접근, 파일 입출력) 처리
- **사용 영역**: 
  - Exposed 트랜잭션 처리 (`tx` 함수)
  - MongoDB 쿼리 처리
  - 외부 서비스 호출 (HTTP 클라이언트)

**Dispatchers.Default**
- **사용 목적**: CPU 집약적인 작업 처리
- **사용 영역**:
  - 복잡한 계산 작업
  - 데이터 변환 작업
  - 정렬 및 집계 처리

**커스텀 Dispatcher**
- 필요에 따라 커스텀 Dispatcher를 생성하여 특정 작업에 최적화된 스레드 풀 활용

#### Exposed 트랜잭션 처리

**newSuspendedTransaction 사용**
- Exposed의 `newSuspendedTransaction`을 직접 사용하여 Coroutine 기반 비동기 트랜잭션 처리
- `Dispatchers.IO`를 명시적으로 지정하여 I/O 작업에 최적화

**커스텀 tx 함수 활용**
```kotlin
// 커스텀 tx 함수 정의
suspend inline fun <T> tx(crossinline block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

// UseCase에서 사용 예시
suspend fun createMember(dto: MemberDTO): MemberDTO {
    val saved = tx { 
        studentSaveService.save(student = prepared) 
    }
    return studentMapper.domainToDTO(domain = saved)
}
```

**장점:**
- 간편한 트랜잭션 관리
- Dispatcher 설정을 중앙에서 관리
- 코드 재사용성 향상

#### 처리 흐름

**비동기 처리 흐름:**
```
HTTP Request
  ↓
Controller (suspend 함수)
  ↓
UseCase (suspend 함수)
  ↓
tx { ... } (Dispatchers.IO)
  ↓
Domain Service (suspend 함수)
  ↓
Repository (suspend 함수)
  ↓
PostgreSQL/MongoDB (Non-Blocking I/O)
  ↓
응답 반환 (Non-Blocking)
```

**Non-Blocking 처리 특징:**
- 스레드가 블로킹되지 않아 다른 요청 동시 처리 가능
- 높은 동시성과 처리량 확보
- 시스템 리소스 효율적 활용

### 7.3 Application 외부 연동 방식

#### Async + Non-Blocking 패턴

AMS는 **Async + Non-Blocking 패턴**을 통해 외부 시스템과 연동합니다.

**처리 원칙:**
- 외부 API 호출은 비동기로 처리
- 응답 대기 중에도 다른 작업 처리 가능
- Non-Blocking I/O를 통한 높은 동시성 확보

#### 외부 연동 사례

**1. 휴대폰 인증 서비스 연동**
- **목적**: 회원 가입 및 로그인 시 휴대폰 인증 코드 발급 및 검증
- **처리 방식**: HTTP 클라이언트를 통한 비동기 API 호출
- **특징**: 
  - Non-Blocking I/O로 여러 인증 요청 동시 처리
  - 타임아웃 및 재시도 전략 적용

**2. 알림 발송 서비스 연동**
- **목적**: 출석 확인, 이벤트 발생 시 알림 발송
- **처리 방식**: HTTP 클라이언트를 통한 비동기 API 호출
- **특징**:
  - 이벤트 핸들러에서 비동기로 처리
  - Command 응답 시간에 영향 없음
  - 실패 시 재시도 메커니즘 적용

#### HTTP 클라이언트 사용

**Spring WebClient 또는 Ktor Client**
- **Spring WebClient**: Spring 생태계와의 통합 용이
- **Ktor Client**: Kotlin Coroutine 네이티브 지원
- **선택 기준**: 프로젝트 요구사항 및 팀 선호도

**Non-Blocking HTTP 호출**
```kotlin
// 예시: 알림 발송 서비스 호출
suspend fun sendNotification(event: NotificationEvent) {
    webClient.post()
        .uri("/api/notifications")
        .bodyValue(event)
        .awaitExchange()
        .awaitBody<String>()
}
```

**장점:**
- 스레드 블로킹 없이 외부 API 호출
- 높은 동시성 확보
- 리소스 효율적 활용

#### 타임아웃 및 재시도 전략

**타임아웃 설정**
- **연결 타임아웃**: 연결 설정 시간 제한
- **읽기 타임아웃**: 응답 대기 시간 제한
- **쓰기 타임아웃**: 요청 전송 시간 제한

**재시도 전략**
- **지수 백오프(Exponential Backoff)**: 재시도 간격을 점진적으로 증가
- **최대 재시도 횟수**: 무한 재시도 방지
- **재시도 조건**: 네트워크 오류, 타임아웃 등 일시적 오류에만 재시도

**처리 흐름:**
```
외부 서비스 호출 요청
  ↓
타임아웃 설정 확인
  ↓
비동기 HTTP 호출 (Non-Blocking)
  ↓
성공 → 결과 반환
실패 → 재시도 조건 확인
  ↓
재시도 가능 → 지수 백오프 적용 후 재시도
재시도 불가능 → 오류 처리
```

### 7.4 이벤트 처리 방식

#### 이벤트 기반 비동기 처리

AMS는 **이벤트 기반 비동기 처리**를 통해 시스템 구성 요소 간 느슨한 결합을 달성합니다.

**처리 원칙:**
- 도메인 이벤트를 통한 비동기 통신
- 이벤트 발행자와 수신자의 느슨한 결합
- Command 응답 시간에 영향 없는 이벤트 처리

#### 내부 Application Event Publisher 사용

**현재 구현:**
- Spring의 `ApplicationEventPublisher` 또는 커스텀 내부 이벤트 퍼블리셔 사용
- 동일 JVM 내에서 이벤트 발행 및 처리
- 단일 인스턴스 환경에서 효율적 동작

**이벤트 발행:**
- Command 처리 완료 후 트랜잭션 커밋 완료 시점에 이벤트 발행
- 트랜잭션 롤백 시 이벤트 미발행으로 데이터 일관성 보장

**이벤트 수신 및 처리:**
- 이벤트 핸들러가 비동기로 이벤트 수신
- 별도의 스레드에서 이벤트 처리
- Non-Blocking 처리로 다른 작업에 영향 없음

#### 이벤트 핸들러의 비동기 처리

**이벤트 핸들러 유형:**

**1. MongoDB 동기화 핸들러**
- **역할**: PostgreSQL 데이터를 MongoDB로 동기화
- **처리 방식**: Coroutine을 통한 비동기 처리
- **특징**: 조회 최적화된 데이터 구조로 변환 후 저장

**2. 알림 발송 핸들러**
- **역할**: 이벤트 발생 시 외부 알림 서비스 호출
- **처리 방식**: 비동기 HTTP 클라이언트를 통한 외부 서비스 호출
- **특징**: Command 응답 시간에 영향 없음

**3. 통계 업데이트 핸들러**
- **역할**: 데이터 변경 시 통계 데이터 업데이트
- **처리 방식**: 비동기로 통계 데이터 계산 및 저장
- **특징**: 배치 처리로 성능 최적화

**처리 흐름:**
```
Command 처리 완료
  ↓
트랜잭션 커밋 완료
  ↓
도메인 이벤트 발행 (MemberCreated 등)
  ↓
이벤트 퍼블리셔가 이벤트 전달
  ↓
이벤트 핸들러 비동기 수신 (Coroutine)
  ↓
이벤트 처리 (MongoDB 동기화, 알림 발송 등)
  ↓
처리 완료
```

#### 향후 서드파티 메시지 큐 도입 예정

**도입 예정 이유:**
- **분산 처리**: 여러 인스턴스 간 이벤트 공유 및 처리 분산
- **메시지 영속성**: 메시지 손실 방지 및 재처리 지원
- **확장성**: 이벤트 처리 부하 분산 및 수평 확장 용이
- **모니터링**: 메시지 큐 레벨에서 이벤트 처리 현황 모니터링 가능

**검토 중인 기술:**
- **Kafka**: 대용량 이벤트 스트림 처리 및 고성능 이벤트 브로커
- **RabbitMQ**: 유연한 메시징 패턴 지원 및 관리 도구 제공

### 7.5 알림 발송 인터페이스 설계

#### 알림 발송 인터페이스 설계 원칙

AMS는 **Hexagonal Architecture의 Outbound Port 패턴**을 따라 알림 발송 인터페이스를 설계합니다.

**설계 원칙:**
- **추상화**: 알림 발송 로직을 인터페이스로 추상화
- **느슨한 결합**: 외부 알림 서비스와의 느슨한 결합 유지
- **확장성**: 다양한 알림 채널 추가 용이
- **테스트 용이성**: Mock 객체를 통한 쉬운 테스트

#### 추상화된 인터페이스 구조

**Outbound Port (도메인 레이어)**
```kotlin
interface NotificationPort {
    suspend fun sendNotification(notification: Notification): Result<Unit>
}

// 도메인 모델
data class Notification(
    val recipient: String,
    val message: String,
    val channel: NotificationChannel
)

enum class NotificationChannel {
    KAKAO_TALK, SMS, PUSH
}
```

**Outbound Adapter (인프라스트럭처 레이어)**
```kotlin
@Component
class KakaoTalkNotificationAdapter(
    private val webClient: WebClient
) : NotificationPort {
    override suspend fun sendNotification(notification: Notification): Result<Unit> {
        // 카카오 알림톡 API 호출
    }
}
```

**장점:**
- 도메인 레이어가 외부 구현 세부사항에 의존하지 않음
- 알림 채널 추가 시 도메인 로직 변경 불필요
- 테스트 시 Mock 구현체로 대체 가능

#### 향후 지원 예정 채널

**1. 카카오 알림톡 (Kakao Talk)**
- **용도**: 비즈니스 메시지 발송
- **특징**: 템플릿 메시지 지원, 높은 도착률

**2. SMS**
- **용도**: 일반 문자 메시지 발송
- **특징**: 넓은 호환성, 실시간 발송

**3. PUSH 알림**
- **용도**: 모바일 앱 알림 발송
- **특징**: 실시간 푸시 알림, 사용자 참여율 향상

**확장 전략:**
- 각 채널별 Adapter 구현체를 별도로 개발
- Factory 패턴을 통한 적절한 Adapter 선택
- 설정 기반으로 채널 활성화/비활성화 관리

#### 비동기 처리 및 재시도 전략

**비동기 처리**
- 알림 발송은 이벤트 핸들러에서 비동기로 처리
- Command 응답 시간에 영향 없음
- Coroutine을 통한 Non-Blocking 처리

**재시도 전략**
- **지수 백오프**: 재시도 간격을 점진적으로 증가
- **최대 재시도 횟수**: 무한 재시도 방지
- **재시도 조건**: 네트워크 오류, 타임아웃 등 일시적 오류에만 재시도

**오류 처리**
- 최대 재시도 횟수 초과 시 Failed Event Store에 저장
- 운영자가 수동으로 재처리 가능
- 알림 실패에 대한 모니터링 및 알림

**처리 흐름:**
```
이벤트 발생 (출석 확인 등)
  ↓
알림 발송 핸들러 수신
  ↓
NotificationPort 구현체 선택
  ↓
비동기 알림 발송 (Coroutine)
  ↓
성공 → 처리 완료
실패 → 재시도 메커니즘 적용
  ↓
최대 재시도 횟수 초과 → Failed Event Store 저장
```

---

