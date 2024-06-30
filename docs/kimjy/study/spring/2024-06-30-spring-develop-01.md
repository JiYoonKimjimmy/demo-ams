# 나만의 API Application 표준 만들기 

API Application 프로젝트 개발을 위해서 **나만의 개발 표준**을 정리해보고자 한다.

`Hexagonal Architecture` 기반의 패키지 구조부터 `Entity` 공통 클래스, `HTTP Response` 공통 클래스 등
Spring Boot 기반의 API Application 개발에 필요한 표준을 정리한다.

#### 프로젝트 기본 설계

- Package 구조 설계

#### 프로젝트 공통 클래스 설계

- 공통 `Entity` 클래스 설계
- 공통 `Response` 클래스 설계
- 공통 `Pageable Response` 클래스 설계

#### 프로젝트 Infrastructure 구조 설계

- Exception Handler 구조 설계
- Logging 처리 구조 설계
- RestClient 처리 구조 설계

---

## 1. Package 구조 설계

**헥사고날 아키텍처**는 시스템 개발을 위한 아키텍처로 **유연성, 유지보수성, 테스트 용이성** 이란
장점을 프로젝트에게 부여할 수 있는 설계 기법이다.

### Domain 도메인 중심 설계

학생 정보 관리라는 기능 개발을 위해 `student` 라는 도메인 개념을 설정하고 패키지 구조를 다음과 같이 설계하였다.

```
main
└── kotlin
    └── me.jimmyberg.ams
        └── v1
            └── student
                ├── controller
                │   └── model
                ├── repository
                │   └── entity
                └── service
                    └── domain
```

---

## 2. 