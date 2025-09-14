package me.jimmyberg.ams.common.error

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import org.springframework.http.HttpStatusCode

class ErrorResponseTest : CustomStringSpec({

    "STUDENT_MANAGEMENT_SERVICE & STUDENT_NOT_FOUND 예외 발생 시, '1000_001' 에러 코드 & 에러 메시지 생성 정상 확인한다" {
        val featureCode = FeatureCode.STUDENT_MANAGEMENT_SERVICE
        val errorCode = ErrorCode.STUDENT_NOT_FOUND
        val response = ErrorResponse.toResponseEntity(featureCode, errorCode)
        val result = response.body!!

        response.statusCode shouldBe HttpStatusCode.valueOf(404)
        result.result.code shouldBe "1000_001"
        result.result.message shouldBe "Student Management Service is failed: Student not found."
    }

    "MISSING_REQUIRED_DATA 예외 발생 시, '1000_801' 에러 코드 및 에러 메시지 생성 정상 확인한다" {
        val featureCode = FeatureCode.STUDENT_MANAGEMENT_SERVICE
        val errorCode = ErrorCode.MISSING_REQUIRED_DATA
        val detailMessage = "Student 'name' is required"
        val response = ErrorResponse.toResponseEntity(featureCode, errorCode, detailMessage)
        val result = response.body!!

        response.statusCode shouldBe HttpStatusCode.valueOf(400)
        result.result.code shouldBe "1000_801"
        result.result.message shouldBe "Student Management Service is failed: Some required data is missing. Student 'name' is required."
    }

})