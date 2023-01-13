package ru.cft.cardbybin.dto

data class Card(
    val number: CardNumber = CardNumber(),
    val scheme: String = "",
    val type: String = "",
    val brand: String = "",
    val prepaid: Boolean = false,
    val country: CardCountry = CardCountry(),
    val bank: CardBank = CardBank()
)