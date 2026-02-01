# 🏛️ AMS 시스템 아키텍처

---

## 문서 개요

**AMS(Academy Management Service)** 는 학원 운영과 수강 관리를 효율적으로 지원하기 위한 RESTful 기반의 백엔드 서비스입니다.

본 문서는 AMS 시스템의 전체 아키텍처를 14개 섹션으로 구성하여 설명합니다. 각 섹션은 독립적인 문서로 분리되어 있으며, 아래 목차를 통해 접근할 수 있습니다.

### 주요 특징

- **멀티 테넌트 지원**: 여러 학원이 동일한 시스템을 사용
- **CQRS 패턴**: PostgreSQL(Command)과 MongoDB(Query) 분리
- **Hexagonal Architecture**: 포트/어댑터 패턴 기반
- **비동기 처리**: Kotlin Coroutine 기반 Non-Blocking 처리
- **역할 기반 접근 제어**: RBAC 기반 5가지 회원 역할

---

## 목차

| 섹션 | 제목           | 설명                                           | 링크                                                 |
|----|--------------|----------------------------------------------|----------------------------------------------------|
| 1  | 시스템 개요       | 시스템 목적, 범위, 비즈니스 도메인, 외부 시스템 연동              | [바로가기](architecture/01_system_overview.md)         |
| 2  | 아키텍처 다이어그램   | 고수준 아키텍처, 레이어 구조, 컴포넌트 상호작용                  | [바로가기](architecture/02_architecture_diagrams.md)   |
| 3  | 기술 스택 상세     | 언어, 프레임워크, 데이터베이스, 빌드 도구                     | [바로가기](architecture/03_tech_stack.md)              |
| 4  | 아키텍처 패턴 및 원칙 | Hexagonal Architecture, CQRS, EDD, 도메인 모델    | [바로가기](architecture/04_architecture_patterns.md)   |
| 5  | 데이터 아키텍처     | PostgreSQL/MongoDB 역할, CQRS 흐름, 트랜잭션 관리      | [바로가기](architecture/05_data_architecture.md)       |
| 6  | 데이터베이스 모델링   | ERD, PostgreSQL/MongoDB 스키마, 인덱스 전략          | [바로가기](architecture/06_database_modeling.md)       |
| 7  | 비동기 처리 전략    | Coroutine 사용, Non-Blocking 처리, 이벤트 처리        | [바로가기](architecture/07_async_processing.md)        |
| 8  | 보안 아키텍처      | 인증/인가, RBAC, API 보안 정책                       | [바로가기](architecture/08_security_architecture.md)   |
| 9  | 통신 및 연동      | RESTful API 설계, 외부 시스템 연동, API Gateway       | [바로가기](architecture/09_communication.md)           |
| 10 | 확장성 및 성능     | 수평/수직 확장, 로드 밸런싱, 캐싱, 성능 최적화                 | [바로가기](architecture/10_scalability_performance.md) |
| 11 | 모니터링 및 로깅    | 로깅 전략, 모니터링 도구, 에러 추적 및 알림                   | [바로가기](architecture/11_monitoring_logging.md)      |
| 12 | 배포 및 운영      | 배포 전략, CI/CD 파이프라인, 무중단 배포, 설정 관리            | [바로가기](architecture/12_deployment_operations.md)   |
| 13 | 에러 처리        | 글로벌 예외 처리, 에러 응답 표준화, 재시도, Circuit Breaker   | [바로가기](architecture/13_error_handling.md)          |
| 14 | 테스트 전략       | 단위 테스트, 통합 테스트, Fixtures, 커버리지 목표            | [바로가기](architecture/14_test_strategy.md)           |
| 15 | API 문서화 전략   | Spring RestDocs, Kotest 통합, AsciiDoc 템플릿, 배포 | [바로가기](architecture/15_api_documentation.md)       |

---

## 관련 문서

- [요구사항 문서](00_ams_requirement.md)
- [아키텍처 작성 체크리스트](01_ams_system_architecture_checklist.md)

---

## 문서 버전

- **최종 업데이트**: 2025-12-22
- **문서 상태**: 완료 (15/15 섹션)
