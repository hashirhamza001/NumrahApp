package com.app.numrahapp.views

import android.util.Log
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MyWebSocketListener() : WebSocketListener() {
    companion object {
        lateinit var webSocket: WebSocket
        var isAccepted: Boolean = false
        var chatId: String = ""
        var udid: String = ""
    }

    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        Log.e("WebSocket Connected!", "trye")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.e("Message received:", "$text")
        WebSocketManager.setMessage(text)

    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.e("Message received (bytes):", " ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.e("WebSocket Closing:", "$code / $reason")
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.e("WebSocket Closed:", "$code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        Log.e("WebSocket Error:", " ${t.message}")
    }
}



