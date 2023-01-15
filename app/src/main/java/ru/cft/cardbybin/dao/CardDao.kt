package ru.cft.cardbybin.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.cft.cardbybin.entity.BinEntity

@Dao
interface CardDao {
    @Query("SELECT * FROM BinEntity")
    fun getAllBins(): LiveData<List<BinEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bin: BinEntity)
}