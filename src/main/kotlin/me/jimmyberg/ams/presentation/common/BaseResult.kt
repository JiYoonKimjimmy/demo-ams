package me.jimmyberg.ams.presentation.common

import me.jimmyberg.ams.infrastructure.common.enumerate.Result
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.FeatureCode

data class BaseResult(
    val status: Result = Result.SUCCESS,
    val code: String? = null,
    val message: String? = null
) {

    constructor(featureCode: FeatureCode, errorCode: ErrorCode, detailMessage: String? = null): this(
        status = Result.FAILED,
        code = "${featureCode.code}_${errorCode.code}",
        message = buildFailureMessage(featureCode, errorCode, detailMessage)
    )

    private companion object {
        fun buildFailureMessage(featureCode: FeatureCode, errorCode: ErrorCode, detailMessage: String?): String =
            buildString {
                append(featureCode.message)
                append(" is failed: ")
                append(errorCode.message)
                append(".")
                if (detailMessage != null) {
                    append(" ")
                    append(detailMessage)
                    append(".")
                }
            }
    }
}