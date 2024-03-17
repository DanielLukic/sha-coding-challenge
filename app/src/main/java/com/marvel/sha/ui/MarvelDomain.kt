package com.marvel.sha.ui

import androidx.paging.PagingData
import com.marvel.sha.domain.MarvelCharacter
import com.marvel.sha.domain.MarvelComic
import com.marvel.sha.domain.MarvelCreator
import kotlinx.coroutines.flow.Flow

internal interface MarvelDomain {
    fun characterList(query: String): Flow<PagingData<MarvelCharacter>>
    fun comicList(query: String): Flow<PagingData<MarvelComic>>
    fun creatorList(query: String): Flow<PagingData<MarvelCreator>>
    suspend fun characterDetail(id: String): MarvelCharacter
    suspend fun comicDetail(id: String): MarvelComic
    suspend fun creatorDetail(id: String): MarvelCreator
}
