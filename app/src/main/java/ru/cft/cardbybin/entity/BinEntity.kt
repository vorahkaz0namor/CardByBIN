package ru.cft.cardbybin.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BinEntity(
    @PrimaryKey
    val bin: Int
) {
    fun toDto() = bin

    companion object {
        fun fromDto(bin: Int) = BinEntity(bin)
    }
}
