package ru.cft.cardbybin.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val bin: Int
) {
    fun toDto() = bin

    companion object {
        fun fromDto(bin: Int) = BinEntity(0, bin)
    }
}
