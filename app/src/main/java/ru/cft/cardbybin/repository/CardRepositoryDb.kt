package ru.cft.cardbybin.repository

import androidx.lifecycle.LiveData

interface CardRepositoryDb {
    fun getAllBins(): LiveData<List<Int>>
    fun saveBin(bin: Int)
}