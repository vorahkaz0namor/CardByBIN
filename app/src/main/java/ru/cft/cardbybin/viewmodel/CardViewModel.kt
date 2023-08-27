package ru.cft.cardbybin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.cft.cardbybin.db.AppDb
import ru.cft.cardbybin.dto.BinList
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.model.FeedModel
import ru.cft.cardbybin.repository.CardRepositoryDb
import ru.cft.cardbybin.repository.CardRepositoryDbImpl
import ru.cft.cardbybin.repository.CardRepositoryNet
import ru.cft.cardbybin.repository.CardRepositoryNetImpl

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryNet: CardRepositoryNet = CardRepositoryNetImpl()
    private val repositoryDb: CardRepositoryDb =
        CardRepositoryDbImpl(AppDb.getInstance(application).cardDao())
    private val _binList = MutableLiveData(BinList())
    val binListForView: LiveData<BinList>
        get() = _binList
    private val _cardInfo = MutableLiveData(FeedModel())
    val cardInfo: LiveData<FeedModel>
        get() = _cardInfo
    private var currentCardBin: String? = null
    private lateinit var showingBin: String

    fun getCardInfo(bin: Int) {
        _cardInfo.value = _cardInfo.value?.loading()
        repositoryNet.getCardInfo(bin, object : CardRepositoryNet.CardCallback<Card> {
            override fun onSuccess(result: Card) {
                showingBin = bin.toString()
                repositoryDb.saveBin(bin)
                getBinHistory()
                _cardInfo.postValue(_cardInfo.value?.showing(result))
            }
            override fun onError(e: Exception) {
                Log.d("WE'VE GOT AN EXCEPTION:", e.toString())
                _cardInfo.postValue(_cardInfo.value?.error(e.toString()))
            }
        })
    }

    private fun getBinHistory() {
        _binList.postValue(
            _binList.value?.copy(binList = repositoryDb.getAllBins())
        )
    }

    fun saveCurrentBin(bin: String?) {
        currentCardBin = bin
    }

    fun getCurrentBin() = currentCardBin

    fun getShowingBin() = showingBin
}