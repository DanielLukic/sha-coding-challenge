package com.marvel.sha.data.characters

import com.marvel.sha.data.MarvelAuthentication
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.retrofit
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Query

internal class CharacterService(
    private val client: OkHttpClient,
    private val authentication: MarvelAuthentication,
) {
    private val api by lazy { retrofit(client, Api::class) }

    suspend fun pagedAt(offset: Int): MarvelResponse = with(authentication()) {
        api.pagedAt(ts, apikey, hash, offset)
    }

    private interface Api {
        @GET("characters")
        suspend fun pagedAt(
            @Query("ts") timestamp: String,
            @Query("apikey") apikey: String,
            @Query("hash") hash: String,
            @Query("offset") offset: Int,
            //@Query("limit") limit: Int, seems to break the api for me - keeping default for now
        ): MarvelResponse
    }
}
