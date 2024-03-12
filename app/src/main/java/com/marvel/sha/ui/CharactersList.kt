package com.marvel.sha.ui

import androidx.paging.PagingData
import com.marvel.sha.domain.MarvelCharacter
import kotlinx.coroutines.flow.Flow

/** Required by UI to query paging data for characters screen. */
internal interface CharactersList {
    fun observe(): Flow<PagingData<MarvelCharacter>>
}
