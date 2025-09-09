package me.jimmyberg.ams.infrastructure.error.exception

import me.jimmyberg.ams.infrastructure.error.ErrorCode

class ResourceNotFoundException(errorCode: ErrorCode) : BaseException(errorCode)