package com.marvel.sha.data.ktordelight

import bx.logging.Log
import com.marvel.sha.data.BASE_URL
import com.marvel.sha.data.MarvelAuthentication
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.string
import com.marvel.sha.domain.MarvelAttribution
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal class ComicService(
    private val client: HttpClient,
    private val authentication: MarvelAuthentication,
    private val attribution: MarvelAttribution,
) {
    suspend fun comics(offset: Int, query: String) = client.get(BASE_URL + "comics") {
        url {
            Log.verbose { "request comics $offset $query" }
            with(authentication()) {
                parameter("ts", ts)
                parameter("apikey", apikey)
                parameter("hash", hash)
            }
            if (query.isNotBlank()) parameter("titleStartsWith", query)
            parameter("formatType", "comic")
            parameter("offset", offset)
            parameter("orderBy", "title")
        }
    }.body<MarvelResponse>().also {
        it.data.results.forEachIndexed { idx, it ->
            Log.verbose { "#${offset + idx}: ${it.string("title")}" }
        }
        attribution.value = it.attributionText ?: attribution.value
    }

}
