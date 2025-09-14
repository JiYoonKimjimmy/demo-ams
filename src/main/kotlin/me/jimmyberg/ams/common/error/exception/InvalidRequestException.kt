package me.jimmyberg.ams.common.error.exception

import me.jimmyberg.ams.common.error.ErrorCode

class InvalidRequestException(errorCode: ErrorCode, detailMessage: String? = null) : BaseException(errorCode, detailMessage)