package me.jimmyberg.ams.v1.student.controller

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.v1.student.controller.model.SaveStudentRequest
import me.jimmyberg.ams.v1.student.controller.model.StudentModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatusCode
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class StudentControllerTest @Autowired constructor(
    private val studentController: StudentController
): BehaviorSpec({

//    extension(SpringExtension)

    given("새로운 학생 정보 정상 입력하는 경우") {
        val request = SaveStudentRequest(
            student = StudentModel(
                name = "김모아",
                phone = "01012341234",
                birthday = "19900202",
                gender = Gender.FEMALE,
                schoolName = "여의도중",
                schoolType = SchoolType.MIDDLE,
                grade = 1
            )
        )

        `when`("학생 이름 & 생년월일 & 휴대폰번호 모두 신규 등록이라면") {
            val response = studentController.saveStudent(request)
            then("학생 정보 등록 정상 성공한다") {
                response.shouldNotBeNull()
                response.statusCode shouldBe HttpStatusCode.valueOf(200)
            }
        }
    }
})