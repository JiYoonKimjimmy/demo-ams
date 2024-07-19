package me.jimmyberg.ams.infra.error.exception

import me.jimmyberg.ams.infra.error.ErrorCode

class ResourceNotFoundException(errorCode: ErrorCode) : BaseException(errorCode)