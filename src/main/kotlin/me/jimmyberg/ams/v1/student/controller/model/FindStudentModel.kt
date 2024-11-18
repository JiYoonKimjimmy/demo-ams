package me.jimmyberg.ams.v1.student.controller.model

import me.jimmyberg.ams.common.domain.PageableContent
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.enumerate.StudentStatus
import me.jimmyberg.ams.common.model.BaseResponse
import me.jimmyberg.ams.common.model.PageableModel
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.v1.student.service.domain.Student
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class FindStudentResponse(
    val student: StudentModel
) : BaseResponse<FindStudentResponse>() {
    override fun success(httpStatus: HttpStatus): ResponseEntity<FindStudentResponse> {
        return ResponseEntity(this, httpStatus)
    }
}

data class ScrollStudentsRequest(
    val id: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val birth: String? = null,
    val gender: Gender? = null,
    val schoolName: String? = null,
    val schoolType: SchoolType? = null,
    val grade: Int? = null,
    val status: StudentStatus? = null,
    val pageable: PageableRequest = PageableRequest()
)

data class ScrollStudentsResponse(
    val pageable: PageableModel,
    val content: List<StudentModel>
) : BaseResponse<ScrollStudentsResponse>() {

    constructor(pageableContent: PageableContent<Student>, mapper: (Student) -> StudentModel): this(
        pageable = PageableModel.from(pageableContent),
        content = pageableContent.content.map(mapper)
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<ScrollStudentsResponse> {
        return ResponseEntity(this, httpStatus)
    }

}