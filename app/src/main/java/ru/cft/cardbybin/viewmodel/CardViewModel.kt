package ru.cft.cardbybin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.http.HTTP_OK
import ru.cft.cardbybin.dto.Card
import ru.cft.cardbybin.model.FeedModel
import ru.cft.cardbybin.repository.CardRepository
import ru.cft.cardbybin.util.AndroidUtils.hotFlow
import ru.cft.cardbybin.util.CompanionCardByBin.exceptionCode
import ru.cft.cardbybin.util.CompanionCardByBin.SAMPLE_BIN
import ru.cft.cardbybin.util.CompanionCardByBin.customLog
import ru.cft.cardbybin.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    private lateinit var _binList: Flow<List<Int>>
    val binListForView: Flow<List<Int>>
        get() = _binList
    private val _cardState = MutableLiveData(FeedModel())
    val cardState: LiveData<FeedModel>
        get() = _cardState
    private var _cardInfo = MutableLiveData(Card())
    val cardInfo: LiveData<Card>
        get() = _cardInfo
    private val _eventOccurrence = SingleLiveEvent(HTTP_OK)
    val eventOccurrence: LiveData<Int>
        get() = _eventOccurrence
    private var _currentCardBin = SAMPLE_BIN
    val currentCardBin: Int
        get() = _currentCardBin

    init {
        @OptIn(ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            _binList = repository.getAllBins()
                .mapLatest { it }
                .hotFlow(viewModelScope, emptyList())
        }
        getCard()
    }

    fun setCurrentBin(bin: String) {
        viewModelScope.launch {
            _currentCardBin = bin.toInt()
        }
    }

    fun getCardByCurrentBin(bin: String) {
        setCurrentBin(bin)
        getCard()
    }

    private fun getCard() {
        viewModelScope.launch {
            try {
                _cardState.value = _cardState.value?.loading()
                _cardInfo.value = repository.getCardInfo(currentCardBin)
                _cardState.value = _cardState.value?.showing()
                _eventOccurrence.value = HTTP_OK
            } catch (e: Exception) {
                customLog("GET CARD INFO", e)
                _cardState.value = _cardState.value?.error()
                _eventOccurrence.value = exceptionCode(e)
            }
        }
    }
}