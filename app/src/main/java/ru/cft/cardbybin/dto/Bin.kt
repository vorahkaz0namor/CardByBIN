package ru.cft.cardbybin.dto

data class Bin(val bin: String) {
    fun binToInt() = if (bin.isNotBlank()) bin.toInt() else 0
}
