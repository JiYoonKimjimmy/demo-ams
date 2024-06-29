package me.jimmyberg.ams.v1.student.service

import io.kotest.core.spec.style.BehaviorSpec

class FindStudentServiceTest : BehaviorSpec({

    given("학생 정보 단건 조회 요청하는 경우") {
        `when`("'신길초'를 다니는 '김모간' 이름과 일치한 정보가 없다면") {
            then("404 Resource Not Found 예외 발생한다") {

            }
        }
        `when`("'신길초'를 다니는 '김모군' 이름의 동일한 정보 다건 조회된다면") {
            then("500 유효하지 않는 정보 예외 발생한다") {

            }
        }
        `when`("'신길초'를 다니는 '김모건' 이름의 동일한 정보 단건 조회된다면") {

            then("회원 단건 조회 결과 정보 생성하여 응답 처리한다") {

            }
        }
    }

})