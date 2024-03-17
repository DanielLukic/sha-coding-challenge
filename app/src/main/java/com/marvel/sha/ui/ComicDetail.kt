package com.marvel.sha.ui

import com.marvel.sha.domain.MarvelComic

internal interface ComicDetail {
    suspend fun retrieve(comicId: String): MarvelComic
}
