package ru.cft.cardbybin.repository

import ru.cft.cardbybin.dto.Card

interface CardRepositoryNet {
    fun getCardInfo(bin: Int, callback: CardCallback<Card>)

    interface CardCallback<T> {
        fun onSuccess(result: T)
        fun onError(e: Exception)
    }
}