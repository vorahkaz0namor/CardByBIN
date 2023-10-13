package ru.cft.cardbybin.dao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.cft.cardbybin.db.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DaoModule {
    @Singleton
    @Provides
    fun provideCardDao(
        appDb: AppDb
    ) : CardDao = appDb.cardDao()
}