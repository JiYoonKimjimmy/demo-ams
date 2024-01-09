# Academy Management Service Project

## AMS <small>학원 관리 서비스 </small>

- 학원 운영을 위한 서비스 제공
- 학원 구성원 및 수업 일정 관리 기능 제공
- 학생별 시험 일정 관리와 시험 결과/성과에 대한 관리 기능 제공

---

### Project 기능

- 학원 구성원 정보 관리
  - 학생/학부모 정보 관리
  - 강사 정보 관리
- 수업 정보 관리
  - 수업 일정 관리
  - 수업 출석 관리
- 시험 정보 관리
  - 시험 일정 관리
  - 시험 결과 관리

---

### Project 구성 Spec

- Kotlin 1.8.22
- Spring Boot 3.1.3
- MongoDB 7.0
- Gradle 8.2.1

### Project 개발 목적

- **클린 아키텍처** 개념 적용한 프로젝트 설계
- **단위** 테스트 코드 작성
- **Kotlin with Coroutine** 활용한 병렬 처리 코드 작성
- 비동기 동시성 트랜잭션 처리
- **Docker** 기반 시스템 환경 구축

---

### Project 상세 기능

#### 학원 구성원 관리

- 학생/학부모/강사 정보 관리
  - 기본 정보
  - 학생 학교/학년 정보 관리
  - 학부모 긴급 연락처 정보 관리
  - 강사 등급 관리

#### 수업 정보 관리

- 수업 정보 관리
- 수업 일정 관리
- 보강 수업 관리

#### 수업 출석 관리

- 수업별 학생 등록 관리
- 수업별 학생 출석 관리
- 결석 학생 보강 수업 관리

#### 시험 정보 관리

- 시험 정보 관리
- 시험 접수 정보 관리
- 시험 성과 관리
  - 시험 참여율/합격율/탈략율 통계 관리

---
