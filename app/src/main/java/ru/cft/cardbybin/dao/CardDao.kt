package ru.cft.cardbybin.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.cft.cardbybin.entity.BinEntity

@Dao
interface CardDao {
    @Query("SELECT * FROM BinEntity")
    fun getAllBins(): Flow<List<BinEntity>>

    @Insert(onConflict = IGNORE)
    suspend fun saveBin(bin: BinEntity)
}