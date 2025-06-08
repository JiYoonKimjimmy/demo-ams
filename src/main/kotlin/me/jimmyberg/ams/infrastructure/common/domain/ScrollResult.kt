package me.jimmyberg.ams.infrastructure.common.domain

data class ScrollResult<R>(
    val content: List<R>,
    val size: Int,
    val isEmpty: Boolean,
    val hasNext: Boolean,
) {

    companion object {
        fun <T, R> from(result: ScrollResult<T>, mapper: (T) -> R): ScrollResult<R> {
            return ScrollResult(
                content = result.content.map(mapper),
                size = result.size,
                isEmpty = result.isEmpty,
                hasNext = result.hasNext,
            )
        }
    }

}