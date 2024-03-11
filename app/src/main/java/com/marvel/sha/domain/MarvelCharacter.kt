package com.marvel.sha.domain

import com.marvel.sha.data.MarvelUrl

internal data class MarvelCharacter(
    val id: Int,
    val name: String,
    val description: String,
    val resourceURI: String,
    val urls: List<MarvelUrl> = emptyList(),
    val thumbnail: MarvelImage,
    val comics: MarvelCollection,
    val stories: MarvelCollection,
    val events: MarvelCollection,
    val series: MarvelCollection,
)
