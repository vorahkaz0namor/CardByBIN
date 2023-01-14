package ru.cft.cardbybin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.cft.cardbybin.R
import ru.cft.cardbybin.db.AppDb
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.model.FeedModel
import ru.cft.cardbybin.repository.CardRepositoryDb
import ru.cft.cardbybin.repository.CardRepositoryDbImpl
import ru.cft.cardbybin.repository.CardRepositoryNet
import ru.cft.cardbybin.repository.CardRepositoryNetImpl
import kotlin.concurrent.thread

class CardViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryNet: CardRepositoryNet = CardRepositoryNetImpl()
    private val repositoryDb: CardRepositoryDb =
        CardRepositoryDbImpl(AppDb.getInstance(application).cardDao())
    private val _binList = MutableLiveData(FeedModel())
    val binList: LiveData<FeedModel>
        get() = _binList
    val cardInfo = MutableLiveData(Card())

    fun getCardInfo(bin: Int) {
        thread { repositoryDb.saveBin(bin) }
        repositoryNet.getCardInfo(bin, object : CardRepositoryNet.CardCallback<Card> {
            override fun onSuccess(result: Card) {
                cardInfo.postValue(result)
            }
            override fun onError(e: Exception) {
                Log.d("WE'VE GOT AN EXCEPTION:", e.toString())
            }
        })
    }

    fun binHistory() {
        thread {

        }
    }
}