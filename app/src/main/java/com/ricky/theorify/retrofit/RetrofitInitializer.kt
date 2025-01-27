package com.ricky.theorify.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ricky.theorify.service.APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {
    private val gson: Gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://theorify.mooo.com/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun apiService() = retrofit.create(APIService::class.java)
}