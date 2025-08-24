package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.application.model.StudentModel
import me.jimmyberg.ams.presentation.common.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CreateStudentRequest(
    val student: StudentModel
)

data class CreateStudentResponse(
    val student: StudentModel
) : BaseResponse<CreateStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<CreateStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}