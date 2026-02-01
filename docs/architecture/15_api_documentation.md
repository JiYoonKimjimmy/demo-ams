# 15. API 문서화 전략

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)

---

### 15.1 API 문서화 도구 개요

AMS는 테스트 기반의 정확한 API 문서 자동화를 위해 **Spring RestDocs**를 표준 문서화 도구로 채택했습니다. RestDocs는 [14. 테스트 전략](14_test_strategy.md) 문서와 긴밀히 연계되어 테스트 코드에서 문서를 생성하므로, API 문서와 실제 구현의 불일치를 방지합니다.

#### 문서화 도구 비교

| 항목             | Spring RestDocs      | Swagger/OpenAPI              |
|----------------|----------------------|------------------------------|
| **생성 방식**      | 테스트 기반 (Test-Driven) | 어노테이션 기반 (Annotation-Driven) |
| **문서 정확성**     | 높음 (테스트 실패 시 빌드 실패)  | 중간 (코드와 문서 불일치 가능)           |
| **코드 침투성**     | 낮음 (프로덕션 코드 무영향)     | 높음 (Controller에 어노테이션 추가)    |
| **테스트 필수 여부**  | 필수                   | 선택                           |
| **UI 제공**      | 정적 HTML              | 대화형 Swagger UI               |
| **커스터마이징**     | 높음 (AsciiDoc 템플릿)    | 중간                           |
| **API 실행 테스트** | 불가                   | 가능 (Try it out)              |

#### 선택 이유

1. **테스트와 문서의 일체화**: 통합 테스트를 통과한 API만 문서화되므로 문서 신뢰도가 보장됩니다.
2. **프로덕션 코드 분리**: Controller에 문서화 어노테이션이 추가되지 않아 비즈니스 로직에 집중할 수 있습니다.
3. **정적 문서 배포**: 빌드 시 HTML로 변환되어 JAR 내부(`/static/docs`)에 포함되며, 별도 UI 서버 없이 제공됩니다.
4. **Kotest 통합**: [3.5 테스트 프레임워크](03_tech_stack.md#35-테스트-프레임워크) 문서에서 채택한 `Kotest` 와 완벽하게 호환됩니다.

---

### 15.2 Spring RestDocs 기술 스택

#### 의존성 구성

| 라이브러리                             | 버전                   | 역할                      |
|-----------------------------------|----------------------|-------------------------|
| **spring-restdocs-webtestclient** | Spring Boot 3.4.4 포함 | WebTestClient 기반 스니펫 생성 |
| **asciidoctor-gradle-plugin**     | 3.3.2                | AsciiDoc → HTML 변환      |
| **spring-restdocs-asciidoctor**   | Spring Boot 포함       | 스니펫 자동 포함 확장            |

#### 빌드 파이프라인 통합

**Gradle 빌드 흐름:**

```
1. test 태스크 실행
   → WebTestClient 테스트 수행
   → build/generated-snippets/ 에 .adoc 스니펫 생성

2. asciidoctor 태스크 실행 (test 완료 후)
   → src/docs/asciidoc/index.adoc 읽기
   → 스니펫 조합하여 HTML 생성
   → build/docs/asciidoc/ 에 출력

3. bootJar 태스크 실행
   → HTML 문서를 JAR의 /static/docs/ 에 포함
   → 배포 시 /docs/ 경로로 접근 가능
```

**build.gradle.kts 핵심 설정:**

- `snippetsDir`: 스니펫 출력 디렉토리 정의
- `test.outputs.dir(snippetsDir)`: 테스트 결과를 스니펫으로 출력
- `asciidoctor.inputs.dir(snippetsDir)`: 스니펫을 문서 소스로 사용
- `bootJar.from(asciidoctor.outputDir)`: HTML을 JAR에 포함

---

### 15.3 Kotest 기반 RestDocs 구현

#### RestDocsBehaviorSpec 커스텀 클래스

**파일 위치:** `D:\00_kjy\workspace\demo-ams\src\test\kotlin\me\jimmyberg\ams\testsupport\restdocs\RestDocsBehaviorSpec.kt`

**역할:**
- Kotest의 BehaviorSpec을 확장하여 RestDocs 설정 자동화
- `ManualRestDocumentation`을 테스트 생명주기에 통합
- 각 테스트 실행 전후로 RestDocs 초기화/종료 처리

**핵심 구현:**

```kotlin
abstract class RestDocsBehaviorSpec(
    protected val webApplicationContext: WebApplicationContext
) : CustomBehaviorSpec({}) {

    protected lateinit var mockMvc: MockMvc
    protected lateinit var webTestClient: WebTestClient
    private val restDocumentation = ManualRestDocumentation()

    init {
        aroundTest { (testCase, execute) ->
            if (testCase.type == TestType.Test) {
                restDocumentation.beforeTest(javaClass, testCase.name.testName)
                mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .build()
                webTestClient = MockMvcWebTestClient
                    .bindTo(mockMvc)
                    .filter(WebTestClientRestDocumentation
                        .documentationConfiguration(restDocumentation))
                    .build()
                val result = execute(testCase)
                restDocumentation.afterTest()
                result
            } else {
                execute(testCase)
            }
        }
    }
}
```

#### 테스트 코드 작성 패턴

**Given-When-Then 구조와 RestDocs 통합:**

```kotlin
@CustomWebTestClientTest
class StudentManagementControllerTest(
    webApplicationContext: WebApplicationContext
) : RestDocsBehaviorSpec(webApplicationContext) {

    init {
        given("학생 정보 생성 API 요청하여") {
            `when`("신규 학생 정보 입력인 경우") {
                then("'201 Created' 정상 응답 확인한다") {
                    webTestClient
                        .post()
                        .uri("/api/v1/student")
                        .bodyValue(request)
                        .exchange()
                        .expectStatus().isCreated
                        .consumeWith(
                            document(
                                "student/create",
                                requestFields(...),
                                responseFields(...)
                            )
                        )
                }
            }
        }
    }
}
```

**스니펫 생성 규칙:**

- 스니펫 이름: `도메인/액션` 형식 (예: `student/create`, `student/find-one`)
- 에러 케이스: 액션명에 에러 종류 추가 (예: `student/create-duplicate-error`)

---

### 15.4 AsciiDoc 문서 템플릿

#### 템플릿 파일 구조

**파일 위치:** `D:\00_kjy\workspace\demo-ams\src\docs\asciidoc\index.adoc`

**기본 구조:**

```asciidoc
= Demo AMS API Docs
:doctype: book
:icons: font
:source-highlighter: highlightjs
:toc: left
:toclevels: 4
:sectlinks:

[[overview]]
== 개요

AMS(Academy Management System) API 문서입니다.

[[student-api]]
== 학생 관리 API

=== 학생 정보 생성

==== 학생 정보 생성 성공 요청/응답
operation::student/create[snippets='http-request,request-fields,http-response,response-fields']

==== 학생 정보 중복 생성 에러 요청/응답
operation::student/create-duplicate-error[snippets='http-response,response-fields']
```

**operation 매크로:**

- `operation::스니펫명[snippets='포함할_스니펫']` 형식
- 자동으로 `build/generated-snippets/스니펫명/` 하위 파일 포함
- 포함 가능한 스니펫: `http-request`, `request-fields`, `http-response`, `response-fields`, `path-parameters`, `request-parameters` 등

#### 문서 확장 전략

**도메인별 섹션 추가:**

```asciidoc
[[member-api]]
== 회원 관리 API

[[class-api]]
== 수업 관리 API

[[attendance-api]]
== 출석 관리 API
```

각 도메인은 CRUD 순서로 하위 섹션 구성 권장.

---

### 15.5 문서 배포 및 접근

#### JAR 내부 포함 방식

**빌드 결과물 구조:**

```
ams-0.0.1.jar
├── BOOT-INF/
├── META-INF/
└── static/
    └── docs/
        └── index.html  ← RestDocs 생성 HTML
```

**접근 URL:**

- 로컬: `http://localhost:8080/docs/index.html`
- 운영: `https://ams.example.com/docs/index.html`

#### CI/CD 파이프라인 통합

**GitHub Actions 빌드 단계** ([12.2 CI/CD 파이프라인](12_deployment_operations.md#122-cicd-파이프라인-개요) 참고):

```yaml
- name: Run Tests
  run: ./gradlew test
  # → 스니펫 생성

- name: Generate Docs
  run: ./gradlew asciidoctor
  # → HTML 변환

- name: Build JAR
  run: ./gradlew bootJar
  # → 문서 포함 패키징
```

#### 문서 버전 관리

**API 버전별 문서 분리** ([9.4 API 버전 관리 전략](09_communication.md#94-api-버전-관리-전략) 참고):

- `/docs/v1/index.html`: API v1 문서
- `/docs/v2/index.html`: API v2 문서 (추후)

각 버전은 `src/docs/asciidoc/v1/`, `src/docs/asciidoc/v2/` 디렉토리로 분리하여 관리.

---

### 15.6 유지보수 가이드

#### 새 API 추가 시

1. **테스트 코드 작성**: `RestDocsBehaviorSpec` 상속 클래스 작성
2. **스니펫 생성**: `document()` 함수로 요청/응답 필드 문서화
3. **템플릿 업데이트**: `index.adoc`에 새 섹션 추가
4. **빌드 확인**: `./gradlew clean test asciidoctor` 실행 후 `build/docs/asciidoc/index.html` 확인

#### 필드 변경 시

**Breaking Change (필드 삭제/타입 변경):**

- API 버전 증가 필요 (v1 → v2)
- 별도 템플릿 파일 생성
- [9.4 API 버전 관리 전략](09_communication.md#94-api-버전-관리-전략) 참고

**Non-Breaking Change (필드 추가):**

- 기존 테스트 코드의 `responseFields()`에 `.optional()` 필드 추가
- 동일 버전 유지

#### 스니펫 재사용

**공통 스니펫 정의:**

```kotlin
// testsupport 패키지에 공통 스니펫 정의
object CommonSnippets {
    val resultFields = listOf(
        fieldWithPath("result.status").description("SUCCESS/FAILED"),
        fieldWithPath("result.code").optional().description("응답 코드"),
        fieldWithPath("result.message").optional().description("응답 메시지")
    )
}

// 테스트에서 재사용
responseFields(
    fieldWithPath("student.id").description("학생 ID"),
    *CommonSnippets.resultFields.toTypedArray()
)
```

#### 문서 품질 검증

**CI 파이프라인 체크리스트:**

- [ ] 모든 테스트 통과 (스니펫 생성 완료)
- [ ] `asciidoctor` 태스크 성공 (AsciiDoc 변환 완료)
- [ ] `bootJar`에 `/static/docs/` 포함 확인
- [ ] 배포 후 `/docs/index.html` 접근 가능 확인

**주기적 리뷰:**

- 분기별 문서-API 일치 여부 확인
- Deprecated API는 템플릿에 경고 표시 추가

---

## 문서 목차로 돌아가기

[← 메인 문서로 돌아가기](../01_ams_system_architecture.md)
