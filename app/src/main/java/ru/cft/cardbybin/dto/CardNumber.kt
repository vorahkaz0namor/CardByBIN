package ru.cft.cardbybin.dto

data class CardNumber(
    val length: Int = 0,
    val luhn: Boolean = false
)
