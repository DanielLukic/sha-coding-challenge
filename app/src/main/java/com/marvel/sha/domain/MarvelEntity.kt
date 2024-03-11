package com.marvel.sha.domain

import com.marvel.sha.data.MarvelUrl

internal data class MarvelEntity(
    val id: Int,
    val name: String,
    val description: String,
    val resourceURI: String,
    val urls: List<MarvelUrl> = emptyList(),
    val thumbnail: MarvelThumbnail,
    val comics: Map<String, Any> = emptyMap(),
    val stories: Map<String, Any> = emptyMap(),
    val events: Map<String, Any> = emptyMap(),
    val series: Map<String, Any> = emptyMap(),
)
