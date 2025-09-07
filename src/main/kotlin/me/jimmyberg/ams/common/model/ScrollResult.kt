package me.jimmyberg.ams.common.model

data class ScrollResult<R>(
    val content: List<R>,
    val size: Int,
    val isEmpty: Boolean,
    val hasNext: Boolean,
) {

    companion object {
        fun <E, R> of(pair: Pair<List<E>, Boolean>, mapper: (E) -> R): ScrollResult<R> {
            val content = pair.first
            val hasNext = pair.second
            return ScrollResult(
                content = content.map(transform = mapper),
                size = content.size,
                isEmpty = content.isEmpty(),
                hasNext = hasNext,
            )
        }

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