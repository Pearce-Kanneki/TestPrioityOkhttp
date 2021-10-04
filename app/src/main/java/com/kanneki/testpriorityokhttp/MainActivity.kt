package com.kanneki.testpriorityokhttp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cdh.okone.OkOne
import com.google.gson.Gson
import com.kanneki.testpriorityokhttp.databinding.ActivityMainBinding
import com.kanneki.testpriorityokhttp.module.UserDetailData
import com.kanneki.testpriorityokhttp.repository.BaseRepository
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object {
        const val MAX_NUMBER = 6
        val gson = Gson()
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var request: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        OkOne.enableRequestPriority(true) // 啟用優先級功能
        OkOne.setLogEnable(true)

        request = BaseRepository.INSTANCE.userClient().apply {
            dispatcher.maxRequests = 1 // 設定最大並發數量(設定此僅為了方便驗證)
        }
    }

    override fun onStart() {
        super.onStart()

        for (i in 1 until MAX_NUMBER) {
            Log.d(BaseRepository.TAG, "Main Index: $i")
            requestNewCall(request, i) // 設定發送請求設定
        }
    }

    // 請求資料與處理
    private fun requestNewCall(request: OkHttpClient, id: Int) {

        request.newCall(BaseRepository.INSTANCE.requestAPI(id, id))
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(BaseRepository.TAG, "API Failure Message:", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val code = response.code
                    val entity = gson.fromJson<UserDetailData>(
                        response.body?.string(),
                        UserDetailData::class.java
                    )
                    Log.d(BaseRepository.TAG, "$code ${entity.login} ${entity.name}")
                }

            })
    }
}