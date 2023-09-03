package ru.cft.cardbybin.viewmodel

import android.util.Log
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
import ru.cft.cardbybin.util.AndroidUtils.defaultDispatcher
import ru.cft.cardbybin.util.CompanionCardByBin.exceptionCode
import ru.cft.cardbybin.util.CompanionCardByBin.SAMPLE_BIN
import ru.cft.cardbybin.util.SingleLiveEvent
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    private lateinit var _binList: List<Int>
    val binListForView: List<Int>
        get() = _binList
    private val _cardState = MutableLiveData(FeedModel())
    val cardState: LiveData<FeedModel>
        get() = _cardState
    private lateinit var _cardInfo: Flow<Card>
    val cardInfo: Flow<Card>
        get() = _cardInfo
    private val _eventOccurrence = SingleLiveEvent(HTTP_OK)
    val eventOccurrence: LiveData<Int>
        get() = _eventOccurrence
    private var _currentCardBin = SAMPLE_BIN
    val currentCardBin: String
        get() = _currentCardBin.toString()

    init {
        viewModelScope.launch {
            repository.getAllBins().mapLatest { _binList = it }
                .flowOn(defaultDispatcher)
        }
        viewModelScope.launch {
            repository.getCardInfo(_currentCardBin)
                .mapLatest {
                    try {
                        _cardState.value = _cardState.value?.loading()
                        _cardInfo = flowOf(it).flowOn(defaultDispatcher)
                        _cardState.value = _cardState.value?.showing()
                        _eventOccurrence.value = HTTP_OK
                    } catch (e: Exception) {
                        Log.d("GET CARD | V.M.", e.toString())
                        _cardState.value = _cardState.value?.error()
                        _eventOccurrence.value = exceptionCode(e)
                    }
                }
                .flowOn(defaultDispatcher)
                .distinctUntilChanged()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted
                        .WhileSubscribed(stopTimeoutMillis = 7_000),
                    initialValue = Card()
                )
        }
    }

    fun saveCurrentBin(bin: Int) {
        _currentCardBin = bin
    }
}