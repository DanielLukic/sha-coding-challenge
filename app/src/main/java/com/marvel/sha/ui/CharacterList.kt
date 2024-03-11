package com.marvel.sha.ui

import androidx.paging.PagingData
import com.marvel.sha.domain.MarvelEntity
import kotlinx.coroutines.flow.Flow

/** Required by UI to query paging data for characters screen. */
internal interface CharacterList {
    fun observe(): Flow<PagingData<MarvelEntity>>
}
