package me.jimmyberg.ams.domain.service

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.domain.model.School
import me.jimmyberg.ams.domain.model.predicate.SchoolPredicate
import me.jimmyberg.ams.domain.model.predicate.StudentPredicate
import me.jimmyberg.ams.testsupport.kotest.CustomBehaviorSpec

class StudentFindServiceImplTest : CustomBehaviorSpec({

    val studentFixture = dependencies.studentFixture

    val studentSaveService = dependencies.studentSaveService
    val studentFindService = dependencies.studentFindService

    given("학생 정보 단건 조회 요청하여") {
        val name = "김모아"
        val school = School("신길초", SchoolType.PRIMARY, 6)
        val domain = studentFixture.make(name = name, school = school)
        studentSaveService.save(domain)

        `when`("'신길초' 학교의 '김모아' 이름과 일치한 학생 정보 있다면") {
            val predicate = StudentPredicate(name = "김모아", school = SchoolPredicate("신길초", SchoolType.PRIMARY, 6))
            val result = studentFindService.findOne(predicate)

            then("학생 정보 조회 성공 확인한다") {
                result shouldNotBe null
                result.name shouldBe "김모아"
            }
        }
    }

})