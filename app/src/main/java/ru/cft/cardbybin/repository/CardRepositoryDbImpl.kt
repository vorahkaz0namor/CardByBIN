package ru.cft.cardbybin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.cft.cardbybin.dao.CardDao
import ru.cft.cardbybin.entity.BinEntity

class CardRepositoryDbImpl(
    private val dao: CardDao
): CardRepositoryDb {
    override fun getAllBins(): LiveData<List<Int>> =
        Transformations.map(dao.getAllBins()) { list ->
            list.map {
                it.toDto()
            }
        }

    override fun saveBin(bin: Int) {
        dao.insert(BinEntity.fromDto(bin))
    }
}