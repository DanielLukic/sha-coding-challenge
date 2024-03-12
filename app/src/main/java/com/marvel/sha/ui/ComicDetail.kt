package com.marvel.sha.ui

import com.marvel.sha.domain.MarvelComic
import kotlinx.coroutines.flow.Flow

internal interface ComicDetail {
    fun retrieve(comicId: String): Flow<MarvelComic>
}
