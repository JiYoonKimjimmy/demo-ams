package me.jimmyberg.ams.presentation.adapter.inbound

import me.jimmyberg.ams.common.EMPTY
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.presentation.dto.CreateStudentRequest
import me.jimmyberg.ams.presentation.dto.StudentDTOFixture
import me.jimmyberg.ams.testsupport.annotation.CustomWebTestClientTest
import me.jimmyberg.ams.testsupport.restdocs.RestDocsBehaviorSpec
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
    }
}