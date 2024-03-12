package com.marvel.sha.data.comics

import com.marvel.sha.data.BASE_URL
import com.marvel.sha.data.MarvelAuthentication
import com.marvel.sha.data.MarvelResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class ComicsService(
    private val client: HttpClient,
    private val authentication: MarvelAuthentication,
) {
    suspend fun comics(offset: Int): MarvelResponse = client.get(BASE_URL + "comics") {
        url {
            with(authentication()) {
                parameter("ts", ts)
                parameter("apikey", apikey)
                parameter("hash", hash)
            }
            parameter("offset", offset)
        }
    }.body()

}
