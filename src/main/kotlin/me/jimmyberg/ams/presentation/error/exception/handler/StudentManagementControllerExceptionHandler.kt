package me.jimmyberg.ams.presentation.error.exception.handler

import me.jimmyberg.ams.common.error.FeatureCode
import me.jimmyberg.ams.presentation.adapter.inbound.StudentManagementController
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [StudentManagementController::class])
class StudentManagementControllerExceptionHandler : BaseExceptionHandler(FeatureCode.STUDENT_MANAGEMENT_SERVICE)


