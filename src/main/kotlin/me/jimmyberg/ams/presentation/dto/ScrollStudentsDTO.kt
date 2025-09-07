package me.jimmyberg.ams.presentation.dto

import me.jimmyberg.ams.common.model.BaseResponse
import me.jimmyberg.ams.common.model.PageableRequest
import me.jimmyberg.ams.common.model.PageableResponse
import me.jimmyberg.ams.common.model.ScrollResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ScrollStudentsRequest(
    val predicate: StudentDTO = StudentDTO(),
    val pageable: PageableRequest = PageableRequest()
)

data class ScrollStudentsResponse(
    val pageable: PageableResponse,
    val content: List<StudentDTO>
) : BaseResponse<ScrollStudentsResponse>() {

    constructor(scrollResult: ScrollResult<StudentDTO>): this(
        pageable = PageableResponse.from(scrollResult),
        content = scrollResult.content
    )

    override fun success(httpStatus: HttpStatus): ResponseEntity<ScrollStudentsResponse> {
        return ResponseEntity(this, httpStatus)
    }

}