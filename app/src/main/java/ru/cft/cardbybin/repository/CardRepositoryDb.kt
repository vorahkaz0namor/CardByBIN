package ru.cft.cardbybin.repository

interface CardRepositoryDb {
    fun getAllBins(): List<Int>
    fun saveBin(bin: Int)
}