package me.jimmyberg.ams.common.model

data class PageableResponse(
    val size: Int,
    val hasNext: Boolean,
    val isEmpty: Boolean,
) {
    companion object {
        fun <T> from(scrollResult: ScrollResult<T>): PageableResponse {
            return PageableResponse(
                size = scrollResult.size,
                hasNext = scrollResult.hasNext,
                isEmpty = scrollResult.isEmpty,
            )
        }
    }
}