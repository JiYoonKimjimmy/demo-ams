package me.jimmyberg.ams.infrastructure.error.exception.handler

import me.jimmyberg.ams.infrastructure.error.FeatureCode
import me.jimmyberg.ams.presentation.port.inbound.StudentManagementController
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [StudentManagementController::class])
class StudentManagementControllerExceptionHandler : BaseExceptionHandler(FeatureCode.STUDENT_MANAGEMENT_SERVICE)