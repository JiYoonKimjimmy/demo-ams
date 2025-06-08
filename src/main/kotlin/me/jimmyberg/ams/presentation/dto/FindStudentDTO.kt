package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.infrastructure.common.model.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindStudentResponse(
    val student: StudentModel
) : BaseResponse<FindStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<FindStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}