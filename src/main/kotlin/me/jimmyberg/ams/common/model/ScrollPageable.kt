package me.jimmyberg.ams.common.model

data class ScrollPageable(
    val size: Int,
    val hasNext: Boolean,
    val isEmpty: Boolean,
) {
    companion object {
        fun <T> from(scrollResult: ScrollResult<T>): ScrollPageable {
            return ScrollPageable(
                size = scrollResult.size,
                hasNext = scrollResult.hasNext,
                isEmpty = scrollResult.isEmpty,
            )
        }
    }
}