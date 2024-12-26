package com.app.numrahapp.views
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LoginRequest(
    val username: String,
    val password: String
)
data class ChatModel(
    val chatId: String,
    val content: String,
    val time: Long,
    var isOther:Boolean,
    var isSeen:Boolean
)
data class ResponseModel(
    val udid: String,
    val token: String
)

interface ApiService {
    @PUT("auth")
    fun login(
    @Header("Authorization") authHeader: String,
    @Header("X-SESSION-ID") sessionId: String,
    @Header("User-Agent") userAgent: String,
    @Query("devid") devid: String,
    @Query("udid") udid: String,
    @Query("token") token: String,
    @Query("session-id") sessionIdQuery: String,
    @Body loginRequest: LoginRequest
    ): Call<ResponseModel>
}
