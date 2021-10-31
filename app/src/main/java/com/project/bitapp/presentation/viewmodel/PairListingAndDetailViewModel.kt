package com.project.bitapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.project.bitapp.data.api.SocketService
import com.project.bitapp.domain.model.* // ktlint-disable no-wildcard-imports
import com.project.bitapp.domain.usecase.GetPairListingUseCase
import com.project.bitapp.utils.* // ktlint-disable no-wildcard-imports
import com.tinder.scarlet.Message
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class PairListingAndDetailViewModel @Inject constructor(
    private val getPairListingUseCase: GetPairListingUseCase,
    private val getSocketService: SocketService

) :

    ViewModel() {
    // List to display all pairs
    var allPairItem: List<AllPairItem> = ArrayList()

    @Inject
    lateinit var application: Application

    // Socket related variables
    private var isSocketConnected = false
    private var currentTradeData = HashMap<String, MessageSubscribeSchema>()
    private var onSocketConnected = MutableLiveData(false)
    var onCurrentSubscribePairItem = MutableLiveData<AllPairItem>()
    var onCurrentSubscribeTickerData = MutableLiveData<TickerChannel>()
    var onCurrentSubscribeTradeData = MutableLiveData<Trade>()

    // List to display all the trades
    val tradesList = ArrayList<Trade>()

    // Service to get all the pairs list
    fun fetchPairList() = liveData(Dispatchers.IO) {

        emit(Resource.loading(null))
        try {
            if (!checkForInternet(application)) {
                emit(Resource.error("No internet Connection", data = null))
            } else {

                emit(Resource.success(data = getPairListingUseCase.getAllPairList()))
            }
        } catch (exception: Exception) {
            emit(Resource.error(exception.message ?: "Error Occurred!", data = null))
        }
    }

    fun subscribeFlow(item: AllPairItem) {
        onCurrentSubscribePairItem.postValue(item)
        if (isSocketConnected) {
            subscribeTickerChannel(item)
        } else {
            connectSocket(item)
        }
    }

    @SuppressLint("CheckResult")
    private fun observeTradeData() {
        getSocketService.observeTicker()
            .subscribe { list ->
                if (!list.isNullOrEmpty()) {
                    if (list.size > 1 && (list[1] !is String) && (list[1] as List<*>).size == 10) {
                        val xyz = list.toTickerData()
                        onCurrentSubscribeTickerData.postValue(xyz)
                    } else if (list.size > 2 && (list[2] as List<*>).size == 4) {
                        val xyz = list.toTradeData()
                        onCurrentSubscribeTradeData.postValue(xyz)
                    }
                }
            }
    }

    private fun subscribeTickerChannel(item: AllPairItem) {
        unsubscribePreviousTickerChannel()
        getSocketService.sendSubscribeRequest(
            SubscribeRequest(
                SUBSCRIBE_EVENT,
                TICKER_CHANNEL,
                item.symbol
            )
        )

        getSocketService.sendSubscribeRequest(
            SubscribeRequest(
                SUBSCRIBE_EVENT,
                TRADES_CHANNEL,
                item.symbol
            )
        )

        observeTradeData()
    }

    private fun unsubscribePreviousTickerChannel() {
        currentTradeData[TICKER_KEY]?.let { previous ->
            val unSubscribeTickerRequest =
                UnsubscribeTicker(UNSUBSCRIBE_EVENT, previous.chanId)
            getSocketService.sendUnSubscribeRequest(unSubscribeTickerRequest)
        }

        currentTradeData[TRADE_KEY]?.let { previous ->
            val unSubscribeTickerRequest =
                UnsubscribeTicker(UNSUBSCRIBE_EVENT, previous.chanId)
            getSocketService.sendUnSubscribeRequest(unSubscribeTickerRequest)
            tradesList.clear()

            onCurrentSubscribeTradeData.postValue(refreshTradeSignal())
        }
    }

    private fun refreshTradeSignal(): Trade {
        return Trade(0.0, REFRESH_KEY, "", "0.0", "")
    }

    @SuppressLint("CheckResult")
    fun connectSocket(item: AllPairItem?) {
        getSocketService.openWebSocketEvent().subscribe {
            when (it) {
                is WebSocket.Event.OnConnectionOpened<*> -> {
                    Log.d(
                        TAG,
                        "OnConnectionOpened "
                    )
                    isSocketConnected = true
                    onSocketConnected.postValue(true)
                    item?.let { checkedItem ->
                        subscribeTickerChannel(checkedItem)
                    }
                }
                is WebSocket.Event.OnConnectionClosed, is WebSocket.Event.OnConnectionFailed -> {
                    isSocketConnected = false
                    onSocketConnected.postValue(false)
                    Log.d(
                        TAG,
                        "OnConnectionClosed || OnConnectionFailed"
                    )
                }
                is WebSocket.Event.OnMessageReceived -> {
                    val message = it.message
                    processMessage((message as Message.Text).value)
                }
                else -> {
                    Log.d(
                        TAG,
                        "OnConnection Closing"
                    )
                }
            }
        }
    }

    private fun processMessage(messageText: String) {
        val gson = Gson()
        try {
            val message = gson.fromJson(messageText, MessageSubscribeSchema::class.java)
            message?.let { mes ->
                if (mes.event.equals(SUBSCRIBE_EVENT_SUCCESS, ignoreCase = true)) {
                    if (mes.channel.equals(TICKER_CHANNEL, ignoreCase = true)) {
                        currentTradeData[TICKER_KEY] = mes
                    } else if (mes.channel.equals(TRADES_CHANNEL, ignoreCase = true)) {
                        currentTradeData[TRADE_KEY] = mes
                    }
                }
            }
        } catch (ee: Exception) {
            Log.d(
                TAG,
                "Exception Occur"
            )
        }
    }

    fun unsubscribeFromALL() {
        unsubscribePreviousTickerChannel()
        tradesList.clear()
    }
}
