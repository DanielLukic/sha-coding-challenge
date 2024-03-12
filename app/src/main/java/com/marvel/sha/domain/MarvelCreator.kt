package com.marvel.sha.domain

import com.marvel.sha.data.MarvelUrl

internal data class MarvelCreator(
    val id: Int,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val suffix: String,
    val fullName: String,
    val modified: String,
    val urls: List<MarvelUrl> = emptyList(),
    val thumbnail: MarvelImage,
    val series: MarvelCollection,
    val stories: MarvelCollection,
    val comics: MarvelCollection,
    val events: MarvelCollection,
)
