package com.marvel.sha.domain

internal data class MarvelImage(
    val path: String,
    val extension: String,
) {
    val url get() = "$path.$extension"
}
