package com.marvel.sha.data.characters

import com.marvel.sha.data.MarvelResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CharactersService {

    @GET("characters")
    suspend fun characters(
        @Query("ts") timestamp: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        //@Query("limit") limit: Int, seems to break the api for me - keeping default for now
        @Query("offset") offset: Int,
    ): MarvelResponse

    companion object {

        fun create(): CharactersService {

            val logger = HttpLoggingInterceptor().apply { level = BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com/v1/public/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CharactersService::class.java)

        }

    }
}
