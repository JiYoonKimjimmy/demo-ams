package me.jimmyberg.ams.infra.error.exception.handler

import me.jimmyberg.ams.infra.error.FeatureCode
import me.jimmyberg.ams.v1.student.controller.StudentManagementController
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(value = 1)
@RestControllerAdvice(assignableTypes = [StudentManagementController::class])
class StudentManagementControllerExceptionHandler : BaseExceptionHandler(FeatureCode.STUDENT_MANAGEMENT_SERVICE)