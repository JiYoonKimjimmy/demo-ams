package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.common.model.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class UpdateStudentRequest(
    val student: StudentDTO
)

data class UpdateStudentResponse(
    val student: StudentDTO
) : BaseResponse<UpdateStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<UpdateStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}