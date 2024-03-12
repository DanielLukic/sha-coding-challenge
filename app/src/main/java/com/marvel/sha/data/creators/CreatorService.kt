package com.marvel.sha.data.creators

import com.marvel.sha.data.MarvelAuthentication
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.retrofit
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query

internal class CreatorService(
    private val client: OkHttpClient,
    private val authentication: MarvelAuthentication,
) {
    private val api by lazy { retrofit(client, Api::class) }

    suspend fun pagedAt(offset: Int): MarvelResponse = with(authentication()) {
        api.pagedAt(ts, apikey, hash, offset)
    }

    private interface Api {
        @GET("creators")
        suspend fun pagedAt(
            @Query("ts") timestamp: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
            @Query("offset") offset: Int,
        ): MarvelResponse
    }
}
