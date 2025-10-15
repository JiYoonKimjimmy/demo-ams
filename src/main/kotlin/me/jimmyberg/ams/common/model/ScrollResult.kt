package me.jimmyberg.ams.common.model

data class ScrollResult<T>(
    val content: List<T>,
    val size: Int,
    val isEmpty: Boolean,
    val hasNext: Boolean,
) {

    companion object {
        fun <T> of(result: Pair<List<T>, Boolean>): ScrollResult<T> {
            val content = result.first
            val hasNext = result.second
            return ScrollResult(
                content = content,
                size = content.size,
                isEmpty = content.isEmpty(),
                hasNext = hasNext,
            )
        }
    }

    fun <R> convert(mapper: (T) -> R): ScrollResult<R> {
        return ScrollResult(
            content = content.map(transform = mapper),
            size = size,
            isEmpty = isEmpty,
            hasNext = hasNext,
        )
    }

}