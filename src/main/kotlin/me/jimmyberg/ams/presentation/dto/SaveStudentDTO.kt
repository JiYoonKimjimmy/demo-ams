package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.presentation.common.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class SaveStudentRequest(
    val student: StudentModel
)

data class SaveStudentResponse(
    val student: StudentModel
) : BaseResponse<SaveStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<SaveStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}