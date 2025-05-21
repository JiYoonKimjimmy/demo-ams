package me.jimmyberg.ams.infrastructure.error.exception

import me.jimmyberg.ams.infrastructure.error.ErrorCode

class InvalidRequestException(errorCode: ErrorCode) : BaseException(errorCode)