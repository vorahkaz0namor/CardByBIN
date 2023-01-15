package ru.cft.cardbybin.repository

import ru.cft.cardbybin.dao.CardDao
import ru.cft.cardbybin.entity.BinEntity

class CardRepositoryDbImpl(
    private val dao: CardDao
): CardRepositoryDb {
    override fun getAllBins(): List<Int> =
        dao.getAllBins().map { it.toDto() }

    override fun saveBin(bin: Int) {
        dao.insert(BinEntity.fromDto(bin))
    }
}