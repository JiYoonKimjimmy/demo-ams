package me.jimmyberg.ams.presentation.controller

import io.kotest.core.test.TestResult
import io.kotest.core.test.TestType
import io.kotest.extensions.spring.SpringExtension
import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType
import me.jimmyberg.ams.presentation.dto.CreateStudentRequest
import me.jimmyberg.ams.testsupport.annotation.CustomSpringBootTest
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import org.hamcrest.Matchers
import org.springframework.http.MediaType
import org.springframework.restdocs.ManualRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@CustomSpringBootTest
class StudentManagementControllerTest(
    private val webApplicationContext: WebApplicationContext
) : CustomBehaviorSpec() {

    override fun extensions() = listOf(SpringExtension)

    private lateinit var mockMvc: MockMvc
    private val restDocumentation = ManualRestDocumentation()

    init {
        aroundTest { (testCase, execute) ->
            if (testCase.type == TestType.Test) {
                restDocumentation.beforeTest(javaClass, testCase.name.testName)
                mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply<DefaultMockMvcBuilder>(
                        MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
                    )
                    .build()
                val result = execute(testCase)
                restDocumentation.afterTest()
                result
            } else {
                execute(testCase)
            }
        }

        val objectMapper = dependencies.objectMapper

        given("학생 정보 생성 API 요청하여") {
            val createStudentUrl = "/api/v1/student"
            val student = StudentModel(
                name = "김모건",
                phone = "01012341234",
                birth = "19900309",
                gender = Gender.MALE,
                zipCode = "12345",
                baseAddress = "baseAddress",
                detailAddress = "detailAddress",
                schoolName = "신길초",
                schoolType = SchoolType.PRIMARY,
                grade = 6
            )
            val request = CreateStudentRequest(student)

            `when`("신규 학생 정보 입력인 경우") {
                then("'201 CREATED' 정상 응답 확인한다") {
                    val result = mockMvc
                        .post(createStudentUrl) {
                            contentType = MediaType.APPLICATION_JSON
                            content = objectMapper.writeValueAsString(request)
                        }
                        .andDo { print() }

                    result
                        .andExpect {
                            status { isCreated() }
                            content {
                                jsonPath("student.id", Matchers.notNullValue())
                                jsonPath("student.name", Matchers.equalTo("김모건"))
                            }
                        }
                        .andDo {
                            handle(
                                MockMvcRestDocumentation.document(
                                    "student/create",
                                    PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("student.id").type(JsonFieldType.STRING).description("학생 ID").optional(),
                                        PayloadDocumentation.fieldWithPath("student.name").type(JsonFieldType.STRING).description("이름"),
                                        PayloadDocumentation.fieldWithPath("student.phone").type(JsonFieldType.STRING).description("연락처"),
                                        PayloadDocumentation.fieldWithPath("student.birth").type(JsonFieldType.STRING).description("생년월일"),
                                        PayloadDocumentation.fieldWithPath("student.gender").type(JsonFieldType.STRING).description("성별"),
                                        PayloadDocumentation.fieldWithPath("student.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                        PayloadDocumentation.fieldWithPath("student.baseAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                        PayloadDocumentation.fieldWithPath("student.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                        PayloadDocumentation.fieldWithPath("student.schoolName").type(JsonFieldType.STRING).description("학교명"),
                                        PayloadDocumentation.fieldWithPath("student.schoolType").type(JsonFieldType.STRING).description("학교 종류"),
                                        PayloadDocumentation.fieldWithPath("student.grade").type(JsonFieldType.NUMBER).description("학년"),
                                        PayloadDocumentation.fieldWithPath("student.status").type(JsonFieldType.STRING).description("상태").optional(),
                                    ),
                                    PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("student.id").type(JsonFieldType.STRING).description("학생 ID"),
                                        PayloadDocumentation.fieldWithPath("student.name").type(JsonFieldType.STRING).description("이름"),
                                        PayloadDocumentation.fieldWithPath("student.phone").type(JsonFieldType.STRING).description("연락처"),
                                        PayloadDocumentation.fieldWithPath("student.birth").type(JsonFieldType.STRING).description("생년월일"),
                                        PayloadDocumentation.fieldWithPath("student.gender").type(JsonFieldType.STRING).description("성별"),
                                        PayloadDocumentation.fieldWithPath("student.zipCode").type(JsonFieldType.STRING).description("우편번호"),
                                        PayloadDocumentation.fieldWithPath("student.baseAddress").type(JsonFieldType.STRING).description("기본 주소"),
                                        PayloadDocumentation.fieldWithPath("student.detailAddress").type(JsonFieldType.STRING).description("상세 주소"),
                                        PayloadDocumentation.fieldWithPath("student.schoolName").type(JsonFieldType.STRING).description("학교명"),
                                        PayloadDocumentation.fieldWithPath("student.schoolType").type(JsonFieldType.STRING).description("학교 종류"),
                                        PayloadDocumentation.fieldWithPath("student.grade").type(JsonFieldType.NUMBER).description("학년"),
                                        PayloadDocumentation.fieldWithPath("student.status").type(JsonFieldType.STRING).description("학생 상태"),
                                        PayloadDocumentation.fieldWithPath("result.result").type(JsonFieldType.STRING).description("응답 결과"),
                                        PayloadDocumentation.fieldWithPath("result.code").type(JsonFieldType.STRING).description("응답 코드").optional(),
                                        PayloadDocumentation.fieldWithPath("result.message").type(JsonFieldType.STRING).description("응답 메시지").optional()
                                    )
                                )
                            )
                        }
                }
            }
        }
    }
}