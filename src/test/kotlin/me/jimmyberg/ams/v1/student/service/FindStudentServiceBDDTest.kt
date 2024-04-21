package me.jimmyberg.ams.v1.student.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.v1.student.repository.fixture.StudentRepositoryFixture
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import kotlin.jvm.optionals.getOrNull

class FindStudentServiceBDDTest : BehaviorSpec({

    val studentRepository = StudentRepositoryFixture()

    given("학생 정보 단건 조회 요청하는 경우") {
        `when`("'신길초'를 다니는 '김모간' 이름과 일치한 정보가 없다면") {
            then("404 Resource Not Found 예외 발생한다") {
                shouldThrow<NotFoundException> { studentRepository.findStudentByName("김모간", "신길초") }
            }
        }
        `when`("'신길초'를 다니는 '김모군' 이름의 동일한 정보 다건 조회된다면") {
            then("500 유효하지 않는 정보 예외 발생한다") {
                shouldThrow<IllegalArgumentException> { studentRepository.findStudentByName("김모군", "신길초") }
            }
        }
        `when`("'신길초'를 다니는 '김모건' 이름의 동일한 정보 단건 조회된다면") {
            val student = studentRepository.findStudentByName("김모건", "신길초").getOrNull()
            then("회원 단건 조회 결과 정보 생성하여 응답 처리한다") {
                student shouldNotBe null
                student?.name shouldBe "김모건"
                student?.school?.schoolName shouldBe "신길초"
            }
        }
    }

})