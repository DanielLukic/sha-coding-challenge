package com.marvel.sha.domain

internal data class MarvelCollection(
    val items: List<MarvelResource>,
) {
    operator fun invoke() = items
}

internal data class MarvelResource(
    val resourceURI: String,
    val name: String,
    val type: String,
)

internal class MarvelList : ArrayList<Map<String, Any>>()

internal class MarvelResourceList : ArrayList<MarvelResource>()
