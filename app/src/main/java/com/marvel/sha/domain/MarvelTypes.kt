package com.marvel.sha.domain

internal data class MarvelCollection(
    val available: Int,
    val returned: Int,
    val collectionURI: String,
    val items: List<MarvelResource>,
)

internal data class MarvelResource(
    val resourceURI: String,
    val name: String,
    val type: String,
)

internal class MarvelList : ArrayList<Map<String, Any>>()

internal class MarvelResourceList : ArrayList<MarvelResource>()
