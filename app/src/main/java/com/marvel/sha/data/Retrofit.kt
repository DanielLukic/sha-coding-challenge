package com.marvel.sha.data

import kotlin.reflect.KClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal fun <T : Any> retrofit(client: OkHttpClient, api: KClass<T>) = Retrofit.Builder()
    .baseUrl("https://gateway.marvel.com/v1/public/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(api.java)
