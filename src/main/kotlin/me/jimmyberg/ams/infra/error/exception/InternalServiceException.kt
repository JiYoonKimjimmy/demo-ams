package me.jimmyberg.ams.infra.error.exception

import me.jimmyberg.ams.infra.error.ErrorCode

class InternalServiceException(errorCode: ErrorCode) : BaseException(errorCode)