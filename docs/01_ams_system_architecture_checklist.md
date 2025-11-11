# 🏛️ AMS 시스템 아키텍처 문서 작성 체크리스트

> 본 문서는 `01_ams_system_architecture.md` 작성 시 참고용 백업 문서입니다.
> 각 항목을 완료하면 체크박스를 체크하여 진행 상황을 추적할 수 있습니다.

---

## 📋 작성 항목 목록

### 1. 시스템 개요
- [x] 시스템 목적 및 범위 설명
- [x] 주요 비즈니스 도메인 정의
- [x] 시스템 경계 및 외부 시스템과의 관계 설명

### 2. 아키텍처 다이어그램
- [x] 고수준 아키텍처 다이어그램 (시스템 전체 구성도)
- [x] 레이어 아키텍처 다이어그램 (Hexagonal Architecture 기반 포트/어댑터 구성)
- [x] 컴포넌트 간 상호작용 다이어그램

### 3. 기술 스택 상세
- [x] 각 기술의 선택 이유 및 역할
- [x] 버전 정보 명시 (Kotlin, Spring Boot, Exposed 등)
- [x] 기술 간 통합 방식 설명

### 4. 아키텍처 패턴 및 원칙
- [x] EDD(Event-Driven Design) 구조 및 도메인 모델 설명
- [x] CQRS 구현 방식 상세 설명
  - [x] Command/Query 분리 전략
  - [x] Event Sourcing 적용 여부
- [x] Hexagonal Architecture 적용 방안 및 포트/어댑터 구조

### 5. 데이터 아키텍처
- [x] PostgreSQL과 MongoDB의 역할 및 책임 구분
- [x] CQRS 데이터 흐름 상세 설명
  - [x] Command 처리: PostgreSQL
  - [x] Query 처리: MongoDB
  - [x] 데이터 동기화 전략
- [x] 트랜잭션 관리 방식 (분산 트랜잭션 처리 전략)

### 6. 비동기 처리 전략
- [x] Coroutine 사용 영역 및 사용 이유
- [x] Application 내부 Thread 처리 방식 (Coroutine 기반 Non-Blocking)
- [x] Application 외부 연동 방식 (Async + Non-Blocking)
- [x] 이벤트 처리 방식
- [x] 알림 발송 인터페이스 설계

### 7. 보안 아키텍처
- [x] 인증/인가 전략
  - [x] 휴대폰 인증 (6자리 코드 발급 및 검증)
  - [x] 로그인 방식 (로그인ID + 비밀번호, 휴대폰 인증 + 비밀번호)
  - [x] JWT 또는 세션 기반 인증 여부
- [x] 역할 기반 접근 제어 (RBAC)
  - [x] 학생, 학부모, 강사, 운영 관리자, 슈퍼 관리자 권한 정의
- [x] API 보안 정책

### 8. 통신 및 연동
- [x] RESTful API 설계 원칙
- [x] 외부 시스템 연동 방식 상세 (Async + Non-Blocking)
- [x] API Gateway 사용 여부
- [x] API 버전 관리 전략

### 9. 확장성 및 성능
- [x] 수평 확장 전략 (멀티 서버 & 멀티 인스턴스)
- [x] 수직 확장 전략
- [x] 로드 밸런싱 전략
- [x] 캐싱 전략 (Redis 등)
- [x] 성능 최적화 방안

### 10. 모니터링 및 로깅
- [x] 로깅 전략
  - [x] 구조화 로그 사용 여부
  - [x] 로그 레벨 및 로깅 포인트
- [x] 모니터링 도구 및 메트릭 수집 방안
- [x] 에러 추적 및 알림 시스템

### 11. 배포 및 운영
- [ ] 배포 전략 (Blue-Green, Canary, Rolling 등)
- [ ] CI/CD 파이프라인 개요
- [ ] 무중단 배포 방안
- [ ] 설정 관리 (Configuration Management)
- [ ] 환경별 설정 전략 (Dev, Staging, Prod)

### 12. 에러 처리
- [ ] 글로벌 예외 처리 전략
- [ ] 에러 응답 형식 표준화
- [ ] 재시도 메커니즘
- [ ] Circuit Breaker 패턴 적용 여부

### 13. 테스트 전략
- [ ] 단위 테스트 전략 (Kotest)
- [ ] 통합 테스트 전략
- [ ] 테스트 Fixtures 활용
- [ ] 테스트 커버리지 목표

---

## 📝 작성 시 참고 사항

### 기존 요구사항 문서 참조
- `00_ams_requirement.md`: 기능 요구사항 및 비즈니스 로직
- `02_ams_database.md`: 데이터베이스 설계
- `database/ams_database.md`: 상세 데이터베이스 스키마

### 기술 스택 버전 정보
- Kotlin 2.1.10 (참고: README.md)
- Spring Boot 3.4.3 (참고: README.md)
- Kotlin Exposed 0.60.0 (참고: README.md)
- Gradle 8.13 (참고: README.md)

### 핵심 아키텍처 패턴
1. **Hexagonal Architecture**: 포트와 어댑터 패턴 기반
2. **EDD (Event-Driven Design)**: 이벤트 기반 도메인 설계
3. **CQRS**: Command와 Query의 분리

### 주요 비즈니스 도메인
- 학원 정보 관리 (Academy Management)
- 회원 관리 (Member Management)
- 수업 관리 (Class Management)
- 운영 관리 (Operation Management)

---

## ✅ 진행 상태

- **전체 진행률**: 53.8% (7/13 섹션 완료)
- **마지막 업데이트**: 2025-11-02
