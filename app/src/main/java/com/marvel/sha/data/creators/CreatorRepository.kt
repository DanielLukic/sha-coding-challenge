package com.marvel.sha.data.creators

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.marvel.sha.data.ShaRoomDatabase
import com.marvel.sha.data.fromRoom
import com.marvel.sha.domain.MarvelCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("MemberVisibilityCanBePrivate")
internal class CreatorRepository(
    private val database: ShaRoomDatabase,
    private val service: CreatorService,
    private val gson: Gson,
) {
    private val dao by lazy { database.creators() }

    val copyright = MutableStateFlow("")
    val attribution = MutableStateFlow("")

    // in theory get more details from backend. but afaict there aren't any for this api...
    fun creatorDetail(creatorId: String) = dao.byId(creatorId).let { gson.fromRoom(it) }

    fun creatorList(): Flow<PagingData<MarvelCreator>> = Pager(
        config = PagingConfig(
            pageSize = CreatorPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { createPagingSource() },
    ).flow

    private fun createPagingSource() = CreatorPagingSource(dao, gson) { offset ->
        service.pagedAt(offset).also {
            assert(it.code == 200) { it }
            copyright.value = it.copyright ?: ""
            attribution.value = it.attributionText
        }
    }
}
