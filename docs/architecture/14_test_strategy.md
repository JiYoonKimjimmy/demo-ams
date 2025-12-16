# 14. 테스트 전략

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

AMS는 코드 품질과 시스템 안정성을 보장하기 위해 체계적인 테스트 전략을 수립합니다. [4.2 Hexagonal Architecture](04_architecture_patterns.md#42-hexagonal-architecture-포트어댑터-패턴)의 계층 분리와 [12.2 CI/CD 파이프라인](12_deployment_operations.md#122-cicd-파이프라인-개요)의 자동화된 품질 검증과 연계하여 빠른 피드백 루프를 구축합니다.

### 14.1 단위 테스트 전략 (Kotest)

#### 테스트 프레임워크

- **Kotest**: Kotlin 네이티브 테스트 프레임워크로, 다양한 테스트 스타일과 직관적인 어설션을 제공합니다.
- **MockK**: Kotlin 전용 Mocking 라이브러리로, Coroutine과 suspend 함수 모킹을 지원합니다.

#### 테스트 스타일

| 스타일 | 용도 | 특징 |
|--------|------|------|
| **BehaviorSpec** | 비즈니스 로직 검증 | Given-When-Then 구조로 시나리오 기술 |
| **DescribeSpec** | 클래스/함수 단위 테스트 | describe-context-it 구조로 계층적 구성 |
| **StringSpec** | 간단한 단위 테스트 | 최소 보일러플레이트로 빠른 작성 |

#### 계층별 단위 테스트 범위

| 계층 | 테스트 대상 | 검증 항목 |
|------|-------------|-----------|
| **Domain** | Entity, Value Object, Domain Service | 비즈니스 규칙, 상태 전이, 유효성 검증 |
| **Application** | UseCase | 흐름 제어, 트랜잭션 경계, 이벤트 발행 |
| **Infrastructure** | Repository, Adapter | 외부 연동 로직 (Mock/Stub 활용) |

#### 단위 테스트 원칙

- **격리성**: 외부 의존성은 MockK로 대체하여 테스트 대상만 검증합니다.
- **빠른 실행**: 단위 테스트는 DB/네트워크 없이 밀리초 단위로 실행되어야 합니다.
- **명확한 실패 메시지**: Kotest 어설션을 활용하여 실패 원인을 명확히 파악합니다.
- **테스트 당 단일 검증**: 하나의 테스트는 하나의 동작만 검증합니다.

### 14.2 통합 테스트 전략

#### 통합 테스트 범위

| 테스트 유형 | 범위 | 도구 |
|-------------|------|------|
| **Repository 통합** | DB 연동 (PostgreSQL, MongoDB) | Testcontainers |
| **API 통합** | Controller → UseCase → Repository | Spring MockMvc, WebTestClient |
| **이벤트 통합** | 이벤트 발행 → 핸들러 → DB 동기화 | Spring Test Events |
| **외부 연동** | 외부 API 호출 | WireMock |

#### Testcontainers 활용

- **PostgreSQL/MongoDB**: 실제 DB 환경과 동일한 조건에서 테스트합니다.
- **컨테이너 재사용**: 테스트 간 컨테이너를 공유하여 실행 시간을 단축합니다.
- **CI 환경 호환**: GitHub Actions에서 Docker-in-Docker로 실행합니다.

#### 통합 테스트 원칙

- **실제 환경 모방**: Testcontainers로 실제 DB, 메시지 큐 환경을 재현합니다.
- **테스트 격리**: 각 테스트는 독립적으로 실행되며, 테스트 후 데이터를 정리합니다.
- **적절한 범위**: 전체 흐름보다는 통합 지점에 집중하여 테스트합니다.

#### 슬라이스 테스트

- **@WebMvcTest**: Controller 계층만 로드하여 API 엔드포인트를 검증합니다.
- **@DataJpaTest/@DataMongoTest**: Repository 계층만 로드하여 쿼리를 검증합니다.
- **커스텀 슬라이스**: 필요한 Bean만 로드하는 커스텀 어노테이션을 정의합니다.

### 14.3 테스트 Fixtures 활용

#### Fixture 전략

| 구분 | 설명 | 적용 범위 |
|------|------|-----------|
| **Object Mother** | 테스트용 객체 생성 팩토리 | 도메인 엔티티, DTO |
| **Builder 패턴** | 유연한 테스트 데이터 생성 | 복잡한 객체 구성 |
| **Test Data Builder** | 기본값 + 필요한 값만 오버라이드 | 대부분의 테스트 |

#### Fixture 설계 원칙

- **명확한 네이밍**: `MemberFixture.createStudent()`, `AcademyFixture.createActive()` 형태로 의도를 명확히 합니다.
- **불변성**: Fixture는 불변 객체로 생성하여 테스트 간 부작용을 방지합니다.
- **최소 데이터**: 테스트에 필요한 최소한의 데이터만 설정합니다.
- **재사용성**: 공통 Fixture는 `test` 소스셋의 fixtures 패키지에 모아 관리합니다.

#### DB Fixture 관리

- **@Sql**: SQL 스크립트로 테스트 데이터를 초기화합니다.
- **@BeforeEach**: 테스트 메서드별 데이터를 프로그래밍 방식으로 설정합니다.
- **Truncate 전략**: 테스트 후 테이블을 초기화하여 격리를 보장합니다.

### 14.4 테스트 커버리지 목표

#### 커버리지 기준

| 계층 | 라인 커버리지 | 브랜치 커버리지 | 우선순위 |
|------|---------------|-----------------|----------|
| **Domain** | 90% 이상 | 85% 이상 | 최우선 |
| **Application (UseCase)** | 80% 이상 | 75% 이상 | 높음 |
| **Infrastructure** | 70% 이상 | 60% 이상 | 중간 |
| **Presentation** | 60% 이상 | 50% 이상 | 낮음 |

#### 커버리지 측정 도구

- **Kover**: Kotlin 공식 커버리지 도구로, Gradle 플러그인으로 통합합니다.
- **JaCoCo**: Java 기반 커버리지 도구로, SonarQube 연동에 활용합니다.

#### 품질 게이트 연동

- **CI 파이프라인**: PR 머지 전 커버리지 기준 미달 시 빌드를 실패 처리합니다.
- **SonarQube**: 커버리지, 코드 스멜, 중복 코드를 통합 관리합니다.
- **리포트 자동화**: 커버리지 변화량을 PR 코멘트로 자동 게시합니다.

#### 커버리지 제외 대상

- **설정 클래스**: `@Configuration`, `@SpringBootApplication`
- **DTO/Request/Response**: 단순 데이터 전송 객체
- **생성된 코드**: Lombok, MapStruct 등 자동 생성 코드

#### 테스트 품질 지표

| 지표 | 목표 | 측정 방법 |
|------|------|-----------|
| **테스트 실행 시간** | 단위 < 10분, 통합 < 20분 | CI 파이프라인 모니터링 |
| **Flaky 테스트 비율** | 1% 미만 | 테스트 재실행 통계 |
| **테스트 유지보수 비용** | 코드 변경 대비 테스트 변경 최소화 | PR 리뷰 |

---