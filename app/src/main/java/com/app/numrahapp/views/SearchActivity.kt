package com.app.numrahapp.views

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.databinding.ActivitySearchBinding
import com.app.numrahapp.views.MyWebSocketListener.Companion.chatId
import com.app.numrahapp.views.MyWebSocketListener.Companion.isAccepted
import com.app.numrahapp.views.MyWebSocketListener.Companion.udid
import com.app.numrahapp.views.MyWebSocketListener.Companion.webSocket
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private var isAccept: Boolean = false
    lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.circularProgressIndicator.isIndeterminate = true
        apiCall()
        listeners()
    }

    private fun listeners() {
        WebSocketManager.messages.observe(this) { response ->
            try {
                val jsonArray = JSONArray(response)
                val jsonObject = jsonArray.getJSONObject(1)
                if (Utils(this).getResponseVal(response, "matched")) {
                    var isAccepteds = jsonObject.optBooleanSafe("accepted")
                    var chatIds = jsonObject.optStringSafe("chatId", "")
                    var udids = jsonObject.optStringSafe("udid", "")
                    if (chatIds.isNotEmpty()) {
                        if (udids.isNotEmpty()) {
                            Log.e("WebSocketManager", "5")
                            chatId = chatIds
                            udid = udids
                            isAccepted = isAccepteds
                        }
                    }
                }

            } catch (_: Exception) {
            }
            if (chatId.isNotEmpty() && udid.isNotEmpty()) {
                binding.llName.visibility = View.VISIBLE
                binding.circularProgressIndicator.visibility = View.GONE
            }
            if (isAccepted && isAccept) {
                startActivity(Intent(this, ChatActivity::class.java))
                finish()
            }
        }

        binding.tvAccpet.setOnClickListener {
            isAccept = true
            var jsonData = """["accept",{"chatId":"${chatId}"}]"""
            webSocket.send(jsonData)
            binding.llName.visibility = View.GONE
            binding.llWait.visibility = View.VISIBLE
            if (isAccepted) {
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
        binding.tvSkip.setOnClickListener {
            isAccepted = false
            var jsonData = """["leave",{"chatId":"${chatId}"}]"""
            webSocket.send(jsonData)
            chatId = ""
            var jsonDataa = ""
            jsonDataa = """["match",{"id":"${System.currentTimeMillis()}","algo":"R"}]"""
            webSocket.send(jsonDataa)
            binding.circularProgressIndicator.visibility = View.VISIBLE
        }
    }

    private fun connectToSocket() {
        var authToken = Utils(this).getToken()
        var sessionId = System.currentTimeMillis().toString()
//        Log.e("tokenWeb", "-> " + generateToken(sessionId, getDeviceIdSimple(this)))
//        Log.e("SocketData", "->sessionId->" + sessionId)
//        Log.e("SocketData", "->deviceId->" + getDeviceIdSimple(this))
//        Log.e("SocketData", "->token->" + makeToken(sessionId, getDeviceId(this))!!)
        val client = OkHttpClient()
        val request = Request.Builder().url(
            "https://dev.wefaaq.net/@fadfedx?session-id=" + sessionId + "&devid=" + Utils(this).getDeviceIdSimple(
                this
            ) + "&udid=" + Utils(this).getDeviceIdSimple(
                this
            ) + "&token=" + Utils(this).makeToken(
                sessionId, Utils(this).getDeviceId(this)
            )!! + "&auth=" + authToken
        ).addHeader("Sec-WebSocket-Protocol", "v2.fadfedly.com/2.1")
            .addHeader("User-Agent", "Fadfed/4.7.7(Android/14)").build()
        webSocket = client.newWebSocket(request, MyWebSocketListener())
        client.dispatcher.executorService.shutdown()
        var jsonData = """["match",{"id":"${System.currentTimeMillis()}","algo":"R"}]"""
        webSocket.send(jsonData)
    }


    fun apiCall() {
        if (Utils(this).getToken().isNullOrEmpty()) {
            val apiService = RetrofitClient.retrofit.create(ApiService::class.java)
            val username = Utils(this).getUserName()
            val password = "112233"
            val sessionId = System.currentTimeMillis().toString()
            val userAgent = "Fadfed/4.7.7(Android/14)"
            val devid = Utils(this).getDeviceIdSimple(this)
            val udid = Utils(this).getDeviceIdSimple(this) + System.currentTimeMillis().toString()
            val token = Utils(this).generateToken(sessionId, Utils(this).getDeviceIdSimple(this))
            val authHeader = createBasicAuthHeader(devid, token)

            val loginRequest = LoginRequest(username!!, password)
            apiService.login(
                authHeader, sessionId, userAgent, devid, udid, token, sessionId, loginRequest
            ).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>, response: Response<ResponseModel>
                ) {

                    if (response.isSuccessful) {
                        Log.d("API_SUCCESS", "Login successful")
                    } else {
                        Log.e(
                            "API_ERROR",
                            "Error: ${response.code()} - ${response.errorBody()?.string()}"
                        )
                    }
                    Utils(this@SearchActivity).saveToken(response.body()?.token)
                    connectToSocket()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Log.e("API_FAILURE", "Failed: ${t.message}")
                }
            })

        } else {
            connectToSocket()
        }

    }

    fun createBasicAuthHeader(username: String, password: String): String {
        val credentials = "$username:$password"
        return "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
    }

}