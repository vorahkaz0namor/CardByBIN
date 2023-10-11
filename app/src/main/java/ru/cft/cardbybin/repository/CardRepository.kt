package ru.cft.cardbybin.repository

import kotlinx.coroutines.flow.Flow
import ru.cft.cardbybin.dto.Card

interface CardRepository {
    suspend fun getCardInfo(bin: Int): Card
    suspend fun getAllBins(): Flow<List<Int>>
}