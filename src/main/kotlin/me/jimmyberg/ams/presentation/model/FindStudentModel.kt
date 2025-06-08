package me.jimmyberg.ams.presentation.model

import me.jimmyberg.ams.common.domain.ScrollContent
import me.jimmyberg.ams.common.enumerate.ActivationStatus
import me.jimmyberg.ams.common.enumerate.Gender
import me.jimmyberg.ams.common.enumerate.SchoolType
import me.jimmyberg.ams.common.model.BaseResponse
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.common.model.ScrollPageable
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
    val status: ActivationStatus? = null,
    val pageable: PageableRequest = PageableRequest()
)

data class ScrollStudentsResponse(
    val pageable: ScrollPageable,
    val content: List<StudentModel>
) : BaseResponse<ScrollStudentsResponse>() {

    constructor(scrollContent: ScrollContent<StudentModel>): this(
        pageable = ScrollPageable.from(scrollContent),
        content = scrollContent.content
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<ScrollStudentsResponse> {
        return ResponseEntity(this, httpStatus)
    }

}