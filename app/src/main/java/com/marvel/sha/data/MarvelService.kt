package com.marvel.sha.data

import com.marvel.sha.domain.MarvelAttribution
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url

internal class MarvelService(
    private val client: OkHttpClient,
    private val authentication: MarvelAuthentication,
    private val attribution: MarvelAttribution,
) {
    private val api by lazy { retrofit(client, Api::class) }

    suspend fun pagedAt(
        path: String,
        orderBy: String,
        offset: Int,
        query: String,
        filterName: String,
    ): MarvelResponse {
        val auth = with(authentication()) { mapOf("ts" to ts, "apikey" to apikey, "hash" to hash) }
        val search = mapOf("offset" to offset.toString(), "orderBy" to orderBy)
        val filter = if (query.isNotBlank()) mapOf(filterName to query) else emptyMap()
        return api.pagedAt(path, auth, search, filter).also {
            assert(it.code == 200) { it }
            attribution.value = it.attributionText ?: attribution.value
        }
    }

    private interface Api {
        @GET
        suspend fun pagedAt(
            @Url url: String,
            @QueryMap auth: Map<String, String>,
            @QueryMap search: Map<String, String>,
            @QueryMap filter: Map<String, String>,
        ): MarvelResponse
    }
}
