package com.kanneki.testpriorityokhttp.repository

import com.kanneki.testpriorityokhttp.module.UserData
import com.kanneki.testpriorityokhttp.module.UserDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiLinkService {

    @GET("users")
    fun getAll(): Call<List<UserData>>

    @GET("users/{userName}")
    fun getDetailData(@Path("userName") name: String): Call<UserDetailData>
}