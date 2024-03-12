package com.marvel.sha.data.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.marvel.sha.data.MarvelAuthentication
import com.marvel.sha.data.fromRoom
import com.marvel.sha.domain.MarvelCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class CharactersRepository(
    private val database: CharactersDatabase,
    private val service: CharactersService,
    private val authentication: MarvelAuthentication,
    private val gson: Gson,
) {
    private val dao by lazy { database.characters() }

    val attribution = MutableStateFlow("")

    // in theory get more details from backend. but afaict there aren't any for this api...
    fun characterDetail(characterId: String) = dao.byId(characterId).map { gson.fromRoom(it) }

    fun characters(): Flow<PagingData<MarvelCharacter>> = Pager(
        config = PagingConfig(
            pageSize = CharactersPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { createPagingSource() },
    ).flow

    private fun createPagingSource() = CharactersPagingSource(dao, gson) { offset ->
        service.characters(offset).also {
            assert(it.code == 200) { it }
            attribution.value = it.attributionText
        }
    }

    private suspend fun CharactersService.characters(offset: Int) = with(authentication()) {
        characters(ts, apikey, hash, offset)
    }

}
