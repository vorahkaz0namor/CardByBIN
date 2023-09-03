package ru.cft.cardbybin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.cft.cardbybin.dao.CardDao
import ru.cft.cardbybin.entity.BinEntity

@Database(
    entities = [BinEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {
    abstract fun cardDao(): CardDao
}