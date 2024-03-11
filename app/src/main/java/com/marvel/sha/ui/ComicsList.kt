package com.marvel.sha.ui

import androidx.paging.PagingData
import com.marvel.sha.domain.MarvelCharacter
import kotlinx.coroutines.flow.Flow

internal interface ComicsList {
    fun observe(): Flow<PagingData<MarvelCharacter>>
}
