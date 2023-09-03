package ru.cft.cardbybin.model

data class FeedModel(
    val loading: Boolean = false,
    val showing: Boolean = false,
    val error: Boolean = false
) {
    fun loading() = copy(
        loading = true,
        showing = false,
        error = false
    )

    fun showing() = copy(
        loading = false,
        showing = true,
        error = false
    )

    fun error() = copy(
        loading = false,
        showing = false,
        error = true
    )
}