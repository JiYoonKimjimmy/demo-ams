package me.jimmyberg.ams.common.error.exception

import me.jimmyberg.ams.common.error.ErrorCode

open class BaseException(
    val errorCode: ErrorCode,
    var detailMessage: String? = null
): RuntimeException()