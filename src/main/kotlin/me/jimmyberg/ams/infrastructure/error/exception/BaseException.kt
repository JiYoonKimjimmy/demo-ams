package me.jimmyberg.ams.infrastructure.error.exception

import me.jimmyberg.ams.infrastructure.error.ErrorCode

open class BaseException(
    val errorCode: ErrorCode,
    var detailMessage: String? = null
): RuntimeException()