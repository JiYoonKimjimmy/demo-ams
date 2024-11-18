package me.jimmyberg.ams.v1.student.service

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.testsupport.CustomBehaviorSpec
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate
import me.jimmyberg.ams.v1.student.repository.predicate.StudentPredicate.SchoolPredicate
import me.jimmyberg.ams.v1.student.service.domain.Student.School

class FindStudentServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture
    val fakeStudentRepository = dependencies.fakeStudentRepository

    val findStudentService = dependencies.findStudentService

    beforeSpec {
        fakeStudentRepository.clear()
    }

    given("학생 정보 단건 조회 요청하여") {
        val name = "김모아"
        val school = School("신길초", SchoolType.PRIMARY, 6)
        val domain = studentFixture.make(name = name, school = school)
        fakeStudentRepository.save(domain)

        `when`("'신길초' 학교의 '김모아' 이름과 일치한 학생 정보 있다면") {
            val predicate = StudentPredicate(name = "김모아", school = SchoolPredicate("신길초", SchoolType.PRIMARY, 6))
            val result = findStudentService.findOne(predicate)

            then("학생 정보 조회 성공 확인한다") {
                result shouldNotBe null
                result.name shouldBe "김모아"
            }
        }
    }

})