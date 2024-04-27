package me.jimmyberg.ams.v1.student.service

import io.kotest.core.spec.style.BehaviorSpec

class SaveStudentServiceBDDTest : BehaviorSpec({
    given("'김모건' 이란 학생 정보를 저장하는 경우") {
        `when`("동일한 이름 & 생년월일 & 휴대폰번호 등록된 학생 정보가 있다면") {
            then("중복 등록 예외 응답 처리한다.") {
            }
        }
        `when`("이미 등록된 동일한 학생 이름이 있다면'") {
            then("학생 표시 이름을 '김모건2' 로 저장하고 정상 응답 처리한다.") {
            }
        }
        `when`("유효하고 정상적인 학생 정보라면") {
            then("학생 정보를 저장하고 정상 응답 처리한다.") {
            }
        }
    }
})