package me.jimmyberg.ams.v1.student.controller.model

import me.jimmyberg.ams.common.model.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindStudentResponse(
    val student: StudentModel
) : BaseResponse<FindStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<FindStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}

data class FindAllStudentResponse(
    val students: List<StudentModel>
) : BaseResponse<FindAllStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<FindAllStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}