package me.jimmyberg.ams.infra.error.exception

import me.jimmyberg.ams.infra.error.ErrorCode

open class BaseException(val errorCode: ErrorCode): RuntimeException()