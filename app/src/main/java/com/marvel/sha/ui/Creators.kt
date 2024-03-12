package com.marvel.sha.ui

import androidx.paging.PagingData
import com.marvel.sha.domain.MarvelCreator
import kotlinx.coroutines.flow.Flow

internal interface Creators {
    fun observe(): Flow<PagingData<MarvelCreator>>
    fun retrieve(id: String): Flow<MarvelCreator>
}
