package me.jimmyberg.ams.presentation.common

import me.jimmyberg.ams.infrastructure.common.enumerate.Result
import me.jimmyberg.ams.infrastructure.error.ErrorCode
import me.jimmyberg.ams.infrastructure.error.FeatureCode

data class BaseResult(
    val status: Result = Result.SUCCESS,
    val code: String? = null,
    val message: String? = null
) {

    constructor(featureCode: FeatureCode, errorCode: ErrorCode): this(
        status = Result.FAILED,
        code = "${featureCode.code}_${errorCode.code}",
        message = "${featureCode.message} is failed: ${errorCode.message}."
    )

    fun append(msg: String?): BaseResult {
        return copy(message = "${this.message}${msg?.let { " $it." }}")
    }

}