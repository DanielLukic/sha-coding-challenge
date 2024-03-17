package com.marvel.sha.data

internal data class MarvelResponse(
    val code: Int,
    val status: String,
    val copyright: String?,
    val attributionText: String?,
    val data: MarvelData,
)
