# Academy Management Service Project

## AMS - 학원 관리 서비스
- 수업 등록 관리
- 학생별 수업 출석 관리
- 수업별 필요한 보강 수업 등록 관리
- 학생별 시험 일정 관리
- 학생 등록, 시험 등록 및 완료 여부 등 관리에 필요한 서비스 관리

## Project 기능
- 학생 정보 관리
- 수업 정보 관리
- 학생 수업 출석 관리
- 시험 정보 관리
- 시험 일정 관리
- 시험 성과 관리

## Project 구성
- Kotlin
- Spring Boot 3.1.3
- MongoDB 7.0
- Gradle 8.2.1

## Project 목적
- **클린 아키텍처** 개념 적용한 프로젝트 설계
- 단위/통합 테스트 코드 작성
- **Kotlin** 활용한 **Coroutine 코루틴** 코드 작성
- 비동기 동시성 이슈 처리
- Docker 기반 배포 환경 구축

---

## Project 상세 기능

### 학원 구성원 관리
- 학생/학부모/강사 정보 관리
  - 기본 정보
  - 학생 학교/학년 정보 관리
  - 학부모 긴급 연락처 정보 관리
  - 강사 등급 관리

### 수업 정보 관리
- 수업 정보 관리
- 보강 수업 관리

### 수업 출석 관리
- 수업별 학생 참여 목록 관리
- 수업별 학생 출석 관리
- 결석 학생 보강 수업 관리

### 시험 정보 관리
- 시험 정보 관리
- 시험 접수 정보 관리
- 시험 성과 관리
  - 시험 참여율/합격율/탈략율 통계 관리

---

## Project Database 구조

### [EMS Database Modeling](./docs/database/ams_database.md)

### 학생 관련 Table 목록
- 학생 정보 Table
- 학생 부모 정보 Table

### 수업 관련 Table 목록
- 강사 정보 Table
- 수업 기본 정보 Table
- 정규 수업 관리 Table
- 보강 수업 관리 Table
- 학생 출석 목록 Table

### 시험 관련 Table 목록
- 시험 정보 Table
- 시험 일정 Table
- 시험 관리 기관 정보 Table
- 시험 기타 정보 Table (시험 접수 방법 등 컨텐츠 관리)

### Mapping 관련 Table 목록
- 학생 시험 참가 목록 Table
- 학생 시험 결과 목록 Table

### 이력 관련 Table 목록
- 학생 시험 상태 변경 이력 Table
- 학생 시험 상태 알림 이력 Table

---
