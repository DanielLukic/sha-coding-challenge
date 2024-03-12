package com.marvel.sha.data.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.marvel.sha.data.ShaRoomDatabase
import com.marvel.sha.data.fromRoom
import com.marvel.sha.domain.MarvelCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@Suppress("MemberVisibilityCanBePrivate")
internal class CharacterRepository(
    private val database: ShaRoomDatabase,
    private val service: CharacterService,
    private val gson: Gson,
) {
    private val dao by lazy { database.characters() }

    val copyright = MutableStateFlow("")
    val attribution = MutableStateFlow("")

    // in theory get more details from backend. but afaict there aren't any for this api...
    fun characterDetail(characterId: String) = dao.byId(characterId).map { gson.fromRoom(it) }

    fun characterList(): Flow<PagingData<MarvelCharacter>> = Pager(
        config = PagingConfig(
            pageSize = CharacterPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { createPagingSource() },
    ).flow

    private fun createPagingSource() = CharacterPagingSource(dao, gson) { offset ->
        service.pagedAt(offset).also {
            assert(it.code == 200) { it }
            copyright.value = it.copyright ?: ""
            attribution.value = it.attributionText
        }
    }

}
