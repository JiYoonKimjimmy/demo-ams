package me.jimmyberg.ams.common.model

import me.jimmyberg.ams.common.enumerate.Result
import me.jimmyberg.ams.infra.error.ErrorCode
import me.jimmyberg.ams.infra.error.FeatureCode

data class BaseResult(
    val result: Result = Result.SUCCESS,
    val code: String? = null,
    var message: String? = null
) {

    constructor(featureCode: FeatureCode, errorCode: ErrorCode): this(
        result = Result.FAILED,
        code = "${featureCode.code}_${errorCode.code}",
        message = "${featureCode.message} is failed: ${errorCode.message}."
    )

    fun append(message: String?): BaseResult {
        this.message += message?.let { " $message." }
        return this
    }

}