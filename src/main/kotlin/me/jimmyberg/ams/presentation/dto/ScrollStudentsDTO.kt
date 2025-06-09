package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.application.usecase.model.StudentModel
import me.jimmyberg.ams.domain.common.ScrollResult
import me.jimmyberg.ams.infrastructure.common.enumerate.ActivationStatus
import me.jimmyberg.ams.infrastructure.common.enumerate.Gender
import me.jimmyberg.ams.infrastructure.common.enumerate.SchoolType
import me.jimmyberg.ams.presentation.common.BaseResponse
import me.jimmyberg.ams.presentation.common.PageableRequest
import me.jimmyberg.ams.presentation.common.ScrollPageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ScrollStudentsRequest(
    val predicate: StudentModel = StudentModel(),
    val pageable: PageableRequest = PageableRequest()
)

data class ScrollStudentsResponse(
    val pageable: ScrollPageable,
    val content: List<StudentModel>
) : BaseResponse<ScrollStudentsResponse>() {

    constructor(scrollResult: ScrollResult<StudentModel>): this(
        pageable = ScrollPageable.from(scrollResult),
        content = scrollResult.content
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<ScrollStudentsResponse> {
        return ResponseEntity(this, httpStatus)
    }

}