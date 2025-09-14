package me.jimmyberg.ams.common.error.exception

import me.jimmyberg.ams.common.error.ErrorCode

class ResourceNotFoundException(errorCode: ErrorCode) : BaseException(errorCode)