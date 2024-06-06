package me.jimmyberg.ams.v1.parent.service

import io.kotest.core.spec.style.BehaviorSpec

class SaveParentServiceTest : BehaviorSpec({

    given("학부모 정보를 입력하여 저장 요청하면") {
        
        `when`("등록된 학생 정보가 없는 경우") {
        
            then("학생 정보 없다는 예외 발생하여 실패한다") {
            
            }
        }
        
        `when`("등록된 학생의 학부모 정보 모두 등록된 경우") {
            
            then("학부모 등록 불가 예외 발생하여 실패한다") {
                
            }
        }

        `when`("등록된 학생의 '아버지' 학부모 정보 이미 등록된 경우") {

            then("'아버지' 학부모 등록 불가 예외 발생하여 실패한다") {

            }
        }

        `when`("유효한 경우") {

            then("학부모 정보를 정상 저장한다") {

            }
        }
    }

})