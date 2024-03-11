package com.marvel.sha.data

internal data class MarvelCharacters(
    val code: Int,
    val status: String,
    val attributionText: String,
    val data: MarvelData,
)
