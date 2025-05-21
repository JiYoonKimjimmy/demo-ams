package me.jimmyberg.ams.presentation.controller

import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec
import me.jimmyberg.ams.testsupport.annotation.CustomSpringBootTest
import me.jimmyberg.ams.presentation.model.SaveStudentRequest
import me.jimmyberg.ams.presentation.model.StudentModel
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@CustomSpringBootTest
class StudentManagementControllerTest(
    private val mockMvc: MockMvc
) : CustomBehaviorSpec({

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
        val request = SaveStudentRequest(student)

        `when`("신규 학생 정보 입력인 경우") {
            val result = mockMvc
                .post(createStudentUrl) {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
                .andDo { print() }

            then("'201 CREATED' 정상 응답 확인한다") {
                result
                    .andExpect {
                        status { isCreated() }
                        content {
                            jsonPath("student.id", notNullValue())
                            jsonPath("student.name", equalTo("김모건"))
                        }
                    }
            }
        }
    }

})