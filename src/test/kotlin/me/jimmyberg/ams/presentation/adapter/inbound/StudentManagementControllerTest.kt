package me.jimmyberg.ams.presentation.adapter.inbound

import me.jimmyberg.ams.common.EMPTY
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.presentation.dto.CreateStudentRequest
import me.jimmyberg.ams.presentation.dto.ScrollStudentsRequest
import me.jimmyberg.ams.presentation.dto.UpdateStudentRequest
import me.jimmyberg.ams.presentation.dto.StudentDTOFixture
import me.jimmyberg.ams.testsupport.annotation.CustomWebTestClientTest
import me.jimmyberg.ams.testsupport.restdocs.RestDocsBehaviorSpec
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.web.context.WebApplicationContext

@CustomWebTestClientTest
class StudentManagementControllerTest(
    webApplicationContext: WebApplicationContext
) : RestDocsBehaviorSpec(webApplicationContext) {

    val objectMapper = dependencies.objectMapper

    val studentDTOFixture = StudentDTOFixture()

    init {
        given("학생 정보 생성 API 요청하여") {
            val createStudentUrl = "/api/v1/student"

            `when`("신규 학생 정보 입력인 경우") {
                val student = studentDTOFixture.make()
                val request = CreateStudentRequest(
                    name = student.name!!,
                    phone = student.phone!!,
                    birth = student.birth!!,
                    gender = student.gender!!,
                    zipCode = student.zipCode,
                    baseAddress = student.baseAddress,
                    detailAddress = student.detailAddress,
                    schoolName = student.schoolName,
                    schoolType = student.schoolType,
                    grade = student.grade
                )

                then("'201 Created' 정상 응답 확인한다") {
                    webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .exchange()
                        .expectStatus().isCreated
                        .expectBody()
                        .jsonPath("$.student.id").isNotEmpty
                        .jsonPath("$.student.name").isEqualTo("김모건")
                        .consumeWith(
                            document(
                                "student/create",
                                PayloadDocumentation.requestFields(
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처"),
                                    fieldWithPath("birth").type(JsonFieldType.STRING).description("생년월일"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                    fieldWithPath("zipCode").type(JsonFieldType.STRING).description("우편번호").optional(),
                                    fieldWithPath("baseAddress").type(JsonFieldType.STRING).description("기본 주소").optional(),
                                    fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세 주소").optional(),
                                    fieldWithPath("schoolName").type(JsonFieldType.STRING).description("학교명").optional(),
                                    fieldWithPath("schoolType").type(JsonFieldType.STRING).description("학교 종류").optional(),
                                    fieldWithPath("grade").type(JsonFieldType.NUMBER).description("학년").optional(),
                                ),
                                responseFields(
                                    fieldWithPath("student.id").type(JsonFieldType.NUMBER).description("학생 ID"),
                                    fieldWithPath("student.name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("student.phone").type(JsonFieldType.STRING).description("연락처"),
                                    fieldWithPath("student.birth").type(JsonFieldType.STRING).description("생년월일"),
                                    fieldWithPath("student.gender").type(JsonFieldType.STRING).description("성별"),
                                    fieldWithPath("student.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                    fieldWithPath("student.baseAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                    fieldWithPath("student.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                    fieldWithPath("student.schoolName").type(JsonFieldType.STRING).description("학교명"),
                                    fieldWithPath("student.schoolType").type(JsonFieldType.STRING).description("학교 종류"),
                                    fieldWithPath("student.grade").type(JsonFieldType.NUMBER).description("학년"),
                                    fieldWithPath("student.status").type(JsonFieldType.STRING).description("학생 상태"),
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("응답 결과 (SUCCESS/FAILED)"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("응답 코드").optional(),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("응답 메시지").optional()
                                )
                            )
                        )
                }
            }

            `when`("중복된 학생 정보 입력인 경우") {
                val request = CreateStudentRequest(
                    name = "김모아",
                    phone = "01012341235",
                    birth = "19900202",
                    gender = Gender.MALE,
                    zipCode = "12345",
                    baseAddress = "baseAddress",
                    detailAddress = "detailAddress",
                    schoolName = "신길초",
                    schoolType = SchoolType.PRIMARY,
                    grade = 6
                )

                then("'400 Bad Request' 에러 응답 확인한다") {
                    // 첫 번째 학생 생성
                    webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .exchange()
                        .expectStatus().isCreated

                    // 동일한 정보로 두 번째 학생 생성 시도 (중복 에러 발생)
                    webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .exchange()
                        .expectStatus().isBadRequest
                        .expectBody()
                        .jsonPath("$.result.status").isEqualTo("FAILED")
                        .jsonPath("$.result.code").isEqualTo("1000_002")
                        .jsonPath("$.result.message").isEqualTo("Student Management Service is failed: Student with same name, phone, and birth already exists.")
                        .consumeWith(
                            document(
                                "student/create-duplicate-error",
                                responseFields(
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("FAILED"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("1000_002"),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("Student Management Service is failed: Student with same name, phone, and birth already exists.")
                                )
                            )
                        )
                }
            }

            `when`("학생 필수 정보 중 'name' 입력 누락된 경우") {
                val request = CreateStudentRequest(
                    name = EMPTY,
                    phone = "01012341235",
                    birth = "19900202",
                    gender = Gender.MALE,
                    zipCode = "12345",
                    baseAddress = "baseAddress",
                    detailAddress = "detailAddress",
                    schoolName = "신길초",
                    schoolType = SchoolType.PRIMARY,
                    grade = 6
                )

                then("'400 Bad Request' 에러 응답 정상 확인한다") {
                    webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(request))
                        .exchange()
                        .expectStatus().isBadRequest
                        .expectBody()
                        .jsonPath("$.result.status").isEqualTo("FAILED")
                        .jsonPath("$.result.code").isEqualTo("1000_801")
                        .jsonPath("$.result.message").isEqualTo("Student Management Service is failed: Some required data is missing. 'name' is required.")
                        .consumeWith(
                            document(
                                "student/create-missing-name-error",
                                responseFields(
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("FAILED"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("1000_801"),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("Student Management Service is failed: Some required data is missing. 'name' is required.")
                                )
                            )
                        )
                }
            }
        }

        given("학생 정보 조회 API 요청하여") {
            val createStudentUrl = "/api/v1/student"

            `when`("존재하지 않는 학생 ID 조회인 경우") {
                val notExistsId = Long.MAX_VALUE

                then("'404 Not Found' 에러 코드/메시지 검증한다") {
                    webTestClient
                        .get()
                        .uri("/api/v1/student/{id}", notExistsId)
                        .exchange()
                        .expectStatus().isNotFound
                        .expectBody()
                        .jsonPath("$.result.status").isEqualTo("FAILED")
                        .jsonPath("$.result.code").isEqualTo("1000_001")
                        .jsonPath("$.result.message").isEqualTo("Student Management Service is failed: Student not found.")
                        .consumeWith(
                            document(
                                "student/find-one-not-found-error",
                                responseFields(
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("FAILED"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("1000_001"),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("Student Management Service is failed: Student not found.")
                                )
                            )
                        )
                }
            }

            `when`("기존 학생 단건 조회인 경우") {
                val student = studentDTOFixture.make()
                val uniquePhone1 = "010" + System.currentTimeMillis().toString().takeLast(8)
                val createRequest = CreateStudentRequest(
                    name = student.name!!,
                    phone = uniquePhone1,
                    birth = student.birth!!,
                    gender = student.gender!!,
                    zipCode = student.zipCode,
                    baseAddress = student.baseAddress,
                    detailAddress = student.detailAddress,
                    schoolName = student.schoolName,
                    schoolType = student.schoolType,
                    grade = student.grade
                )

                then("'200 OK' 정상 응답 확인한다") {
                    val createdResult = webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(createRequest))
                        .exchange()
                        .expectStatus().isCreated
                        .expectBody()
                        .returnResult()
                    val createdJson = String(createdResult.responseBody!!)
                    val createdId = objectMapper.readTree(createdJson).path("student").path("id").asLong()

                    webTestClient
                        .get()
                        .uri("/api/v1/student/{id}", createdId)
                        .exchange()
                        .expectStatus().isOk
                        .expectBody()
                        .jsonPath("$.student.id").exists()
                        .jsonPath("$.student.name").exists()
                        .consumeWith(
                            document(
                                "student/find-one",
                                responseFields(
                                    fieldWithPath("student.id").type(JsonFieldType.NUMBER).description("학생 ID"),
                                    fieldWithPath("student.name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("student.phone").type(JsonFieldType.STRING).description("연락처"),
                                    fieldWithPath("student.birth").type(JsonFieldType.STRING).description("생년월일"),
                                    fieldWithPath("student.gender").type(JsonFieldType.STRING).description("성별"),
                                    fieldWithPath("student.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                    fieldWithPath("student.baseAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                    fieldWithPath("student.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                    fieldWithPath("student.schoolName").type(JsonFieldType.STRING).description("학교명"),
                                    fieldWithPath("student.schoolType").type(JsonFieldType.STRING).description("학교 종류"),
                                    fieldWithPath("student.grade").type(JsonFieldType.NUMBER).description("학년"),
                                    fieldWithPath("student.status").type(JsonFieldType.STRING).description("학생 상태"),
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("응답 결과 (SUCCESS/FAILED)"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("응답 코드").optional(),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("응답 메시지").optional()
                                )
                            )
                        )
                }
            }
        }

        given("학생 정보 스크롤 조회 API 요청하여") {
            val scrollUrl = "/api/v1/students/scroll"

            `when`("기본 조건으로 스크롤 조회하는 경우") {
                then("'200 OK' 정상 응답 확인한다") {
                    webTestClient
                        .method(HttpMethod.GET)
                        .uri(scrollUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(ScrollStudentsRequest()))
                        .exchange()
                        .expectStatus().isOk
                        .expectBody()
                        .jsonPath("$.pageable.size").exists()
                        .jsonPath("$.pageable.hasNext").exists()
                        .jsonPath("$.pageable.isEmpty").exists()
                        .jsonPath("$.content").isArray
                        .consumeWith(
                            document(
                                "student/scroll",
                                responseFields(
                                    fieldWithPath("pageable.size").type(JsonFieldType.NUMBER).description("응답 컨텐츠 수"),
                                    fieldWithPath("pageable.hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지 존재 여부"),
                                    fieldWithPath("pageable.isEmpty").type(JsonFieldType.BOOLEAN).description("컨텐츠 비어있음 여부"),
                                    fieldWithPath("content").type(JsonFieldType.ARRAY).description("학생 목록"),
                                    fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("학생 ID").optional(),
                                    fieldWithPath("content[].name").type(JsonFieldType.STRING).description("이름").optional(),
                                    fieldWithPath("content[].phone").type(JsonFieldType.STRING).description("연락처").optional(),
                                    fieldWithPath("content[].birth").type(JsonFieldType.STRING).description("생년월일").optional(),
                                    fieldWithPath("content[].gender").type(JsonFieldType.STRING).description("성별").optional(),
                                    fieldWithPath("content[].zipCode").type(JsonFieldType.STRING).description("우편번호").optional(),
                                    fieldWithPath("content[].baseAddress").type(JsonFieldType.STRING).description("기본 주소").optional(),
                                    fieldWithPath("content[].detailAddress").type(JsonFieldType.STRING).description("상세 주소").optional(),
                                    fieldWithPath("content[].schoolName").type(JsonFieldType.STRING).description("학교명").optional(),
                                    fieldWithPath("content[].schoolType").type(JsonFieldType.STRING).description("학교 종류").optional(),
                                    fieldWithPath("content[].grade").type(JsonFieldType.NUMBER).description("학년").optional(),
                                    fieldWithPath("content[].status").type(JsonFieldType.STRING).description("학생 상태").optional(),
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("응답 결과 (SUCCESS/FAILED)"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("응답 코드").optional(),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("응답 메시지").optional()
                                )
                            )
                        )
                }
            }
        }

        given("학생 정보 수정 API 요청하여") {
            val createStudentUrl = "/api/v1/student"

            `when`("수정 대상 학생 정보가 없는 경우") {
                val notExistsId = Long.MAX_VALUE
                val updateRequest = UpdateStudentRequest(
                    student = me.jimmyberg.ams.presentation.dto.StudentDTO(
                        id = notExistsId,
                        name = "변경이름"
                    )
                )

                then("'404 Not Found' 에러 코드/메시지 검증한다") {
                    webTestClient
                        .put()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(updateRequest))
                        .exchange()
                        .expectStatus().isNotFound
                        .expectBody()
                        .jsonPath("$.result.status").isEqualTo("FAILED")
                        .jsonPath("$.result.code").isEqualTo("1000_001")
                        .jsonPath("$.result.message").isEqualTo("Student Management Service is failed: Student not found.")
                        .consumeWith(
                            document(
                                "student/update-not-found-error",
                                responseFields(
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("FAILED"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("1000_001"),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("Student Management Service is failed: Student not found.")
                                )
                            )
                        )
                }
            }

            `when`("기존 학생 정보를 수정하는 경우") {
                val created = studentDTOFixture.make()
                val uniquePhone2 = "010" + (System.nanoTime().toString().takeLast(8))
                val createRequest = CreateStudentRequest(
                    name = created.name!!,
                    phone = uniquePhone2,
                    birth = created.birth!!,
                    gender = created.gender!!,
                    zipCode = created.zipCode,
                    baseAddress = created.baseAddress,
                    detailAddress = created.detailAddress,
                    schoolName = created.schoolName,
                    schoolType = created.schoolType,
                    grade = created.grade
                )

                then("'200 OK' 정상 응답 확인한다") {
                    val createdResult = webTestClient
                        .post()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(createRequest))
                        .exchange()
                        .expectStatus().isCreated
                        .expectBody()
                        .returnResult()
                    val createdJson = String(createdResult.responseBody!!)
                    val id = objectMapper.readTree(createdJson).path("student").path("id").asLong()

                    val updateName = "김수정"
                    val updateRequest = UpdateStudentRequest(
                        student = me.jimmyberg.ams.presentation.dto.StudentDTO(
                            id = id,
                            name = updateName,
                            phone = created.phone,
                            birth = created.birth,
                            gender = created.gender,
                            zipCode = created.zipCode,
                            baseAddress = created.baseAddress,
                            detailAddress = created.detailAddress,
                            schoolName = created.schoolName,
                            schoolType = created.schoolType,
                            grade = created.grade
                        )
                    )

                    webTestClient
                        .put()
                        .uri(createStudentUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(objectMapper.writeValueAsString(updateRequest))
                        .exchange()
                        .expectStatus().isOk
                        .expectBody()
                        .jsonPath("$.student.id").exists()
                        .jsonPath("$.student.name").exists()
                        .consumeWith(
                            document(
                                "student/update",
                                responseFields(
                                    fieldWithPath("student.id").type(JsonFieldType.NUMBER).description("학생 ID"),
                                    fieldWithPath("student.name").type(JsonFieldType.STRING).description("이름"),
                                    fieldWithPath("student.phone").type(JsonFieldType.STRING).description("연락처"),
                                    fieldWithPath("student.birth").type(JsonFieldType.STRING).description("생년월일"),
                                    fieldWithPath("student.gender").type(JsonFieldType.STRING).description("성별"),
                                    fieldWithPath("student.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                    fieldWithPath("student.baseAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                    fieldWithPath("student.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                    fieldWithPath("student.schoolName").type(JsonFieldType.STRING).description("학교명"),
                                    fieldWithPath("student.schoolType").type(JsonFieldType.STRING).description("학교 종류"),
                                    fieldWithPath("student.grade").type(JsonFieldType.NUMBER).description("학년"),
                                    fieldWithPath("student.status").type(JsonFieldType.STRING).description("학생 상태"),
                                    fieldWithPath("result.status").type(JsonFieldType.STRING).description("응답 결과 (SUCCESS/FAILED)"),
                                    fieldWithPath("result.code").type(JsonFieldType.STRING).description("응답 코드").optional(),
                                    fieldWithPath("result.message").type(JsonFieldType.STRING).description("응답 메시지").optional()
                                )
                            )
                        )
                }
            }
        }
    }
}