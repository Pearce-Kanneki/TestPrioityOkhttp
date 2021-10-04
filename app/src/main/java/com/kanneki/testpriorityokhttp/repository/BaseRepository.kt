package com.kanneki.testpriorityokhttp.repository

import android.util.Log
import com.cdh.okone.OkOne
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BaseRepository {

    companion object {
        const val BASE_URL = "https://api.github.com/users/"
        const val TIMEOUT_NUMBER = 30L
        const val TAG = "pearce"
        @JvmStatic val INSTANCE = BaseRepository()
    }

    fun requestAPI(userId: Int, priority: Int): Request {
        return Request.Builder()
            .url("$BASE_URL$userId")
            .tag("userId: $userId Priority: $priority")
            .build()
            .apply {
                OkOne.setRequestPriority(this, priority) // 設定優先權(-10~10之間,數字越大優先全越高)
            }
    }

    fun userClient() = OkHttpClient
        .Builder()
        .connectTimeout(TIMEOUT_NUMBER, TimeUnit.SECONDS)
        .eventListener(object : EventListener() {

            override fun requestHeadersStart(call: Call) {
                //super.requestHeadersStart(call)
                call.request().tag().apply {
                    Log.d(TAG, "requestHeadersStart: $this")
                }
            }
        })
        .build()

    fun userRetrofitClient(userClient: OkHttpClient) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(userClient)
        .build()
}