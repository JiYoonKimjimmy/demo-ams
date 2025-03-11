package me.jimmyberg.ams.infra.error.exception.handler

import io.kotest.matchers.shouldBe
import me.jimmyberg.ams.infra.error.ErrorCode
import me.jimmyberg.ams.infra.error.ErrorResponse
import me.jimmyberg.ams.infra.error.FeatureCode
import me.jimmyberg.ams.infra.error.exception.BaseException
import me.jimmyberg.ams.infra.error.exception.ResourceNotFoundException
import me.jimmyberg.ams.testsupport.kotest.CustomStringSpec
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

class BaseExceptionHandlerTest : CustomStringSpec({

    "ResourceNotFoundException 예외 발생하는 경우, HTTP Status '404' 확인하고, 에러 코드/메시지 확인한다" {
        val errorCode = ErrorCode.STUDENT_NOT_FOUND
        val exception = ResourceNotFoundException(errorCode)
        val handler = BaseExceptionHandler(FeatureCode.STUDENT_MANAGEMENT_SERVICE)
        val handleBaseException = handler::class.java.getDeclaredMethod("handleBaseException", BaseException::class.java)

        handleBaseException.invoke(handler, exception)

        val response = handleBaseException.invoke(handler, exception) as ResponseEntity<*>
        val result = response.body!! as ErrorResponse

        response.statusCode shouldBe HttpStatusCode.valueOf(404)
        result.result.code shouldBe "1000_001"
        result.result.message shouldBe "Student Management Service is failed: Student not found."
    }

})