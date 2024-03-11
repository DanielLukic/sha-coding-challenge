package com.marvel.sha.domain

import com.marvel.sha.data.MarvelUrl

internal data class MarvelComic(
    val id: Int,
    val description: String,
    val digitalId: Int,
    val title: String,
    val issueNumber: String,
    val isbn: String,
    val upc: String,
    val diamondCode: String,
    val ean: String,
    val format: String,
    val pageCount: Int,
    val series: MarvelResource,
    val urls: List<MarvelUrl> = emptyList(),
    val textObjects: MarvelList,
    val variants: MarvelResourceList,
    val collections: MarvelResourceList,
    val collectedIssues: MarvelResourceList,
    val dates: MarvelList,
    val prices: MarvelList,
    val thumbnail: MarvelImage,
    val images: List<MarvelImage>,
    val creators: MarvelCollection,
    val characters: MarvelCollection,
    val stories: MarvelCollection,
    val events: MarvelCollection,
)
