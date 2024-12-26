package com.app.numrahapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import org.json.JSONObject
import java.math.BigInteger
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8


class Utils(context: Context) {
    var AUTH_TOKEN = "AUTH_TOKEN"
    var USERNAME = "USERNAME"
    var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveToken(value: String?) {
        editor.putString(AUTH_TOKEN, value)
        editor.apply()
    }

    fun setUserName(value: String?) {
        editor.putString(USERNAME, value)
        editor.apply()
    }


    fun getToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(USERNAME, null)
    }

    fun getResponseVal(response: String, s: String): Boolean {
        if (response.contains(s)) {
            return true
        }
        return false
    }

    fun generateToken(sessionId: String, deviceId: String): String {
        val algorithm = "HmacMD5"
        val keySpec = SecretKeySpec(sessionId.toByteArray(UTF_8), algorithm)
        val mac = Mac.getInstance(algorithm)
        mac.init(keySpec)
        val hmacBytes = mac.doFinal(deviceId.toByteArray(UTF_8))
        return hmacBytes.joinToString("") { "%02x".format(it) }
    }

    fun makeToken(
        sessionUUID: String?,
        deviceUDID: String?,
    ): String? {
        val key = sessionUUID
        try {
            val hmacmd5 = Mac.getInstance("HmacMD5")
            hmacmd5.init(SecretKeySpec(key!!.toByteArray(), "HmacMD5"))
            val tokenBytes: ByteArray = hmacmd5.doFinal(deviceUDID?.toByteArray())
            return java.lang.String.format(Locale.ROOT, "%032x", BigInteger(1, tokenBytes))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("HardwareIds")
    fun getDeviceIdSimple(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        val androidId =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        Log.e("getDeviceId", "->" + androidId)
        var dID = md5(androidId)
        Log.e("getDeviceIdMd5", "->" + dID)
        return dID
    }

    fun md5(input: String): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return bytes.joinToString("") {
            "%02x".format(it)
        }
    }
}

fun JSONObject.optBooleanSafe(key: String, defaultValue: Boolean = false): Boolean {
    return if (this.has(key)) this.optBoolean(key, defaultValue) else defaultValue
}

fun JSONObject.optStringSafe(key: String, defaultValue: String = ""): String {
    return if (this.has(key) && !this.isNull(key)) this.optString(
        key, defaultValue
    ) else defaultValue
}



