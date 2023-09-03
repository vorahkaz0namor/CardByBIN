package ru.cft.cardbybin.repository

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import ru.cft.cardbybin.dao.CardDao
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.entity.BinEntity
import ru.cft.cardbybin.remote.CardRemoteApi
import ru.cft.cardbybin.util.AndroidUtils.defaultDispatcher
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
    private val cardRemoteApi: CardRemoteApi
): CardRepository {
    override suspend fun getCardInfo(bin: Int): Flow<Card> =
        flow {
            try {
                val cardResponse = cardRemoteApi.getCard(bin)
                if (cardResponse.isSuccessful) {
                    val cardInfo = cardResponse.body()
                        ?: throw HttpException(cardResponse)
                    cardDao.saveBin(BinEntity.fromDto(bin))
                    emit(cardInfo)
                } else
                    throw HttpException(cardResponse)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("GET CARD | REPO:", e.toString())
            }
        }
            .flowOn(defaultDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllBins(): Flow<List<Int>> =
        cardDao.getAllBins().mapLatest {
            it.map(BinEntity::toDto)
        }
            .flowOn(defaultDispatcher)
}