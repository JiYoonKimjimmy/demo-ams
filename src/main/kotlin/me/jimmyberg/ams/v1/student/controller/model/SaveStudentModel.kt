package me.jimmyberg.ams.v1.student.controller.model

import me.jimmyberg.ams.common.model.BaseResponse
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