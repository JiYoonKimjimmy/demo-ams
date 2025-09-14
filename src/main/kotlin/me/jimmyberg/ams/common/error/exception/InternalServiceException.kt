package me.jimmyberg.ams.common.error.exception

import me.jimmyberg.ams.common.error.ErrorCode

class InternalServiceException(errorCode: ErrorCode) : BaseException(errorCode)