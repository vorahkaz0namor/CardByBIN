package ru.cft.cardbybin.model

import ru.cft.cardbybin.dto.Card

data class FeedModel(
    val card: Card = Card(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val showing: Boolean = false
) {
    fun loading() = copy(
        loading = true,
        showing = false,
        error = false
    )

    fun showing(card: Card) = copy(
        loading = false,
        showing = true,
        card = card,
        error = false
    )

    fun error() = copy(
        loading = false,
        showing = false,
        error = true
    )
}