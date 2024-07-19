package me.jimmyberg.ams.infra.error.exception

import me.jimmyberg.ams.infra.error.ErrorCode

class InvalidRequestException(errorCode: ErrorCode) : BaseException(errorCode)