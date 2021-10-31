package com.project.bitapp.data.api

import com.project.bitapp.domain.model.SubscribeRequest
import com.project.bitapp.domain.model.UnsubscribeTicker
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface SocketService {
    @Receive
    fun openWebSocketEvent(): Flowable<WebSocket.Event>

    @Send
    fun sendSubscribeRequest(subscribeRequest: SubscribeRequest)

    @Send
    fun sendUnSubscribeRequest(unSubscribeTickerRequest: UnsubscribeTicker)

    @Receive
    fun observeTicker(): Flowable<List<Any>>
}
