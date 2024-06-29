package me.jimmyberg.ams.v1.student.service

import io.kotest.core.spec.style.BehaviorSpec

class SaveStudentServiceMockTest: BehaviorSpec({

    given("학생 정보 저장을 요청하면") {

        `when`("동일한 학생 이름 & 휴대폰번호 & 생년월일 정보가 있는 경우") {

            then("학생 정보 저장 실패한다") {

            }
        }

        `when`("동일한 학생 이름 이미 저장되어 있는 경우") {

            then("학생 이름 인덱스를 증가시킨 후, 정상 저장한다") {

            }
        }

        `when`("중복없이 신규 학생 정보인 경우") {

            then("학생 정보를 정상 저장한다") {

            }
        }

    }

})