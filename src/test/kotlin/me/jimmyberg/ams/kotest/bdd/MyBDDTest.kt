package me.jimmyberg.ams.kotest.bdd

import io.kotest.core.spec.style.BehaviorSpec

class MyBDDTest : BehaviorSpec({

    given("API 요청을 한다.") {
        `when`("API 요청에 대한 로직 수행 성공한다.") {
            then("성공 응답 정보를 생성하여 응답 처리한다.") {
            }
        }
    }

})