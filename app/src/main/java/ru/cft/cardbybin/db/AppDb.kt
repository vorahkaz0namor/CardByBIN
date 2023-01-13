package ru.cft.cardbybin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.cft.cardbybin.dao.CardDao
import ru.cft.cardbybin.entity.BinEntity

@Database(
    entities = [BinEntity::class],
    version = 1
)
abstract class AppDb : RoomDatabase() {
    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDb::class.java,
                "cardbybin.db"
            )
                .allowMainThreadQueries()
                .build()
    }
}