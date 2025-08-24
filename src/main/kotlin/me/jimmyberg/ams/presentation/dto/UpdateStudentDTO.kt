package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.application.model.StudentModel
import me.jimmyberg.ams.presentation.common.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class UpdateStudentRequest(
    val student: StudentModel
)

data class UpdateStudentResponse(
    val student: StudentModel
) : BaseResponse<UpdateStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<UpdateStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}