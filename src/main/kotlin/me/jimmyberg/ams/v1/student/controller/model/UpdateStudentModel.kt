package me.jimmyberg.ams.v1.student.controller.model

import me.jimmyberg.ams.common.model.BaseResponse
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