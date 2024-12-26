package com.app.numrahapp.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object WebSocketManager {
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    fun setMessage(message: String) {
        _messages.postValue(message)
    }
}
