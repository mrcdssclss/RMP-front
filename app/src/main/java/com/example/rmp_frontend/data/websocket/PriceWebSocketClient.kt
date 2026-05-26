package com.example.rmp_frontend.data.websocket

import com.example.rmp_frontend.data.dto.InstrumentDto
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.data.storage.TokenStorage
import com.example.rmp_frontend.domain.model.Instrument
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class PriceWebSocketClient(
    private val webSocketUrl: String,
    private val tokenStorage: TokenStorage,
    private val okHttpClient: OkHttpClient = OkHttpClient(),
) {
    private val gson = Gson()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _priceUpdates = MutableSharedFlow<Instrument>(extraBufferCapacity = 64)
    private var webSocket: WebSocket? = null

    val priceUpdates: SharedFlow<Instrument> = _priceUpdates

    fun connect() {
        if (webSocket != null) return

        val requestBuilder = Request.Builder().url(webSocketUrl)
        val token = runBlocking { tokenStorage.getToken() }
        if (token != null) {
            requestBuilder.header("Authorization", "${token.tokenType} ${token.accessToken}")
        }

        webSocket = okHttpClient.newWebSocket(
            requestBuilder.build(),
            object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    runCatching {
                        gson.fromJson(text, InstrumentDto::class.java).toDomain()
                    }.onSuccess { instrument ->
                        scope.launch { _priceUpdates.emit(instrument) }
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    this@PriceWebSocketClient.webSocket = null
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    this@PriceWebSocketClient.webSocket = null
                }
            },
        )
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
    }
}
