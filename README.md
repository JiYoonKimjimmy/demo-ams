# Academy Management Service Project
### [AMS Swagger UI](http://localhost:10010/ams/swagger-ui/index.html)
## AMS (학원 관리 서비스)
- 학생별 수업 출석 관리한다.
- 수업별 필요한 보강 수업을 등록/관리한다.
- 학생별 시험 일정/완료 여부 관리한다.
- 학생 등록, 시험 등록 및 완료 여부 등 관리에 필요한 항목들을 사용자가 추가하고, 관리한다.
- 수업별 학생 출석을 관리한다.

## Project 기능
- 학생 정보 관리
- 수업 정보 관리
- 학생 수업 출석 관리
- 시험 정보 관리
- 시험 일정 관리
- 시험 성과 관리

## Project 구성
- Kotlin
- Gradle
- Spring Boot
- Spring Webflux
- Spring data R2DBC
- MariaDB (AWS RDS)

## Project 목적
- `Kotiln` + `Spring Boot` 기반의 `REST API` Core System 구축
- `Spring Webflux` 활용한 `Reactive REST API` Core System 구축
- `Cloud` 환경 구축 및 배포까지 진행
- Core System 완료 후에는 Multi Module Project 로 변경하여, `Web` Service 제공

---

## Project 상세 기능
### 학생 정보 관리
- 학생 기본 정보 등록 및 관리
- 학생 참가 수업 관리
- 학생 참가 시험 관리

### 수업 정보 관리
- 수업 기본 정보 등록 및 관리
- 보강 수업 등록 및 관리

### 학생 수업 출석 관리
- 수업별 학생 참여 목록 관리
- 수업별 학생 출석 관리
- 결석 학생 보강 수업 관리

### 시험 정보 관리
- 시험 기본 정보 등록 및 관리
- 시험 접수 방법 정리 (학부모 안내용)
- 시험 관리 기관 정보 관리

### 시험 일정 관리
- 시험 접수 기간부터 시험 날짜, 결과 발표 기간 등 전반적인 시험 일정 관리
- 학생별 시험 일정 관리
    - 학생 접수 여부
    - 학생 시험 참석 여부
- 시험 관련 알림 서비스
    - 접수 마감 일주일 전 미접수 학생 알림
    - 시험 하루 전 대상 학생 알림
    - 시험 결과 발표 기간 알림

### 시험 성과 관리
- 학생별 시험 결과 정보 관리
    - 시험 참석 여부 입력
    - 시험 결과 입력
- 통계 정보 관리
    - 시험별 합격율 통계
    - 시험별 점수 통계
    - 학생 연령별 점수 통계

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
