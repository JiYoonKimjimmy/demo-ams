package me.jimmyberg.ams.infrastructure.error

import me.jimmyberg.ams.common.model.BaseResult
import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val result: BaseResult = BaseResult()
) {

    companion object {

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
            return ResponseEntity(ErrorResponse(result = BaseResult(featureCode, errorCode)), errorCode.status)
        }

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode, message: String?): ResponseEntity<ErrorResponse> {
            return BaseResult(featureCode, errorCode)
                .takeIf { message != null }
                ?.append(message)
                ?.let { ResponseEntity(ErrorResponse(result = it), errorCode.status) }
                ?: toResponseEntity(featureCode, errorCode)
        }

    }

}