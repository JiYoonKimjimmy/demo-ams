package me.jimmyberg.ams.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.model.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class CreateStudentRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val phone: String,
    @field:NotBlank
    val birth: String,
    @field:NotNull
    var gender: Gender,
    val zipCode: String? = null,
    val baseAddress: String? = null,
    val detailAddress: String? = null,
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null,

) {
    fun toStudentDTO(): StudentDTO {
        return StudentDTO(
            name = name,
            phone = phone,
            birth = birth,
            gender = gender,
            zipCode = zipCode,
            baseAddress = baseAddress,
            detailAddress = detailAddress,
            schoolName = schoolName,
            schoolType = schoolType,
            grade = grade
        )
    }
}

data class CreateStudentResponse(
    val student: StudentDTO
) : BaseResponse<CreateStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<CreateStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}