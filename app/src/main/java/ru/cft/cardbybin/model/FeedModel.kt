package ru.cft.cardbybin.model

data class FeedModel(
    val binList: List<Int> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val showing: Boolean = false
) {
    fun loading() = copy(
        loading = true,
        showing = false,
        error = false
    )

    fun showing(binList: List<Int>) = copy(
        loading = false,
        showing = true,
        binList = binList,
        error = false
    )

    fun error() = copy(
        loading = false,
        showing = false,
        error = true
    )
}