package me.jimmyberg.ams.infrastructure.error

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import org.springframework.http.HttpStatusCode

class ErrorResponseTest : CustomStringSpec({

    "STUDENT_MANAGEMENT_SERVICE & STUDENT_NOT_FOUND 예외 발생 시, '1000_001' 에러 코드와 에러 메시지 생성 정상 확인한다" {
        val featureCode = FeatureCode.STUDENT_MANAGEMENT_SERVICE
        val errorCode = ErrorCode.STUDENT_NOT_FOUND
        val response = ErrorResponse.toResponseEntity(featureCode, errorCode)
        val result = response.body!!

        response.statusCode shouldBe HttpStatusCode.valueOf(404)
        result.result.code shouldBe "1000_001"
        result.result.message shouldBe "Student Management Service is failed: Student not found."
    }

})