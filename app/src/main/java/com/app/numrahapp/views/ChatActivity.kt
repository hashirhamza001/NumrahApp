package com.app.numrahapp.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.databinding.ActivityChatBinding
import com.app.numrahapp.views.MyWebSocketListener.Companion.webSocket
import org.json.JSONArray
import org.json.JSONObject

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    lateinit var adapter: ChatAdapter
    var list = ArrayList<ChatModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ChatAdapter(list)
        binding.rvChat.adapter = adapter


        binding.ivSend.setOnClickListener {
            buttonClick()
        }
        WebSocketManager.messages.observe(this) { response ->
            try {
                val jsonArray = JSONArray(response)
                val jsonObject = jsonArray.getJSONObject(1)
                var chatId = jsonObject.optStringSafe("chatId")
                var ref = jsonObject.optStringSafe("ref")
                var content = jsonObject.optStringSafe("content")
                var time = jsonObject.optLongSafe("ts")
                if (response.contains("seen")) {
                    for (i in list.indices) {
                        if (list[i].chatId == ref) {
                            list[i].isSeen = true
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                if (content.isNotEmpty()) {
                    list.add(ChatModel(chatId, content, time, true, false))
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun buttonClick() {
        if (binding.etChat.text.toString().isEmpty()) {
            Toast.makeText(this@ChatActivity, "Enter Text", Toast.LENGTH_SHORT).show()
        } else {
            val ts = System.currentTimeMillis()
            val jsonData =
                """["send",{"content":"${binding.etChat.text}","id":"${ts}","to":null,"ts":"${ts}"}]"""
            list.add(ChatModel(ts.toString(), binding.etChat.text.toString(), ts, false, false))
            adapter.notifyDataSetChanged()
            binding.rvChat.smoothScrollToPosition((list.size-1))
            webSocket.send(jsonData)
            binding.etChat.setText("")
            Log.e("Message sent: ", "->" + "$jsonData")
        }
    }

    fun JSONObject.optStringSafe(key: String): String {
        return if (this.has(key) && !this.isNull(key)) this.optString(key, "") else ""
    }

    fun JSONObject.optLongSafe(key: String): Long {
        return if (this.has(key) && !this.isNull(key)) this.optLong(key, 0L) else 0L
    }

    override fun onStop() {
        super.onStop()
        var jsonData = """["leave"]"""
        webSocket.send(jsonData)
    }

}
