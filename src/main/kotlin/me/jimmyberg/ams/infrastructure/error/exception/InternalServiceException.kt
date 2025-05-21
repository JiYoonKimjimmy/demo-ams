package me.jimmyberg.ams.infrastructure.error.exception

import me.jimmyberg.ams.infrastructure.error.ErrorCode

class InternalServiceException(errorCode: ErrorCode) : BaseException(errorCode)