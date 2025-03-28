# AMS 

## Academy Management Service Project <small>학원 관리 서비스</small>

- 학원 구성원 및 수업 일정 관리 기능 제공
- 학원 정보 공유 & 추천 서비스 제공
- 학원비 온라인 결제 서비스 제공

---

### Project Features

#### 학원 구성원 정보 관리

- 학생/학부모 정보 관리
- 학원 강사 정보 관리

#### 수업 정보 관리
 
- 수업 일정 관리
- 수업 학생 출석 관리

#### 학원 수강 정보 관리

- 수강 과목
- 수강 대상
  - 추천 대상 범위
- 수강 기간
- 수강 목표
  - 기본 목표
  - 완강 효과
- 수강 교육과정
- 강사 정보 & 소개
- 수강 & 강사 평가

---

### Project Spec

- Kotlin 2.1.10
- Spring Boot 3.4.3
- Kotlin Exposed 0.60.0
- Gradle 8.13
- H2 Database

---

### Trouble Shooting 😈

### Kotlin & Spring 주의사항

#### @Transactional 사용시 주의점

- `Kotlin` 클래스는 기본적으로 `final` 상태
- `Spring AOP` 적용을 위해서는 Proxy 객체 생성을 위해 외부에서도 접근 가능한 접근 지시자 필요

##### 해결 방안

- `open` 접근 지시자 키워드 직접 추가
- `kotlin-spring` 플러그인 사용

```kotlin
plugins {
    kotlin("plugin.spring")
}
```

> `kotlin-spring` 플러그인
> 
> `@Component`, `@Transactional`, `@Service` 등의 `Spring Annotation` 붙은 클래스를 자동으로 open 상태로 변경

> IntelliJ 2024.1+ 버전 주의사항
> 
> `K2` 컴파일러 적용한다면, `Kotlin`, `Spring F/W` 코드 분석 강화

---

### Kotest 실행 시 `WeakPairMap$Pair$Weak` 에러

#### 발생 원인
- JVM 버전과 Kotest 버전 간의 호환성 문제
- 테스트 실행 시 클래스 로딩 관련 문제

#### 해결 방법

##### 1. Gradle 테스트 실행 시 포크 모드 비활성화

```kotlin
tasks.withType<Test> {
    useJUnitPlatform()
    forkEvery = 0  // Disable forking
}
```

##### 2. IntelliJ IDEA 설정

- Settings > Build, Execution, Deployment > Build Tools > Gradle
- Run tests using: `IntelliJ IDEA` 선택

##### 3. Kotest 최신 버전 사용

- 프로젝트 `libs.versions.toml` 파일에서 `Kotest` 버전을 최신 버전으로 업데이트

---
