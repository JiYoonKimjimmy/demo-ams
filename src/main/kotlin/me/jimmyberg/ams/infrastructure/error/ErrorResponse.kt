package me.jimmyberg.ams.infrastructure.error

import me.jimmyberg.ams.presentation.common.BaseResult
import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val result: BaseResult = BaseResult()
) {

    companion object {

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
            return ResponseEntity(ErrorResponse(result = BaseResult(featureCode, errorCode)), errorCode.status)
        }

        fun toResponseEntity(featureCode: FeatureCode, errorCode: ErrorCode, detailMessage: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity(ErrorResponse(result = BaseResult(featureCode, errorCode, detailMessage)), errorCode.status)
        }

    }

}