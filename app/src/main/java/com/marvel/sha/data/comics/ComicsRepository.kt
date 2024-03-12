package com.marvel.sha.data.comics

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.marvel.sha.domain.MarvelComic
import commarvel.sha.datacomics.ComicsQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("MemberVisibilityCanBePrivate")
internal class ComicsRepository(
    private val database: ComicsQueries,
    private val service: ComicsService,
    private val gson: Gson,
) {
    val copyright = MutableStateFlow("")
    val attribution = MutableStateFlow("")

    fun comics(): Flow<PagingData<MarvelComic>> = Pager(
        config = PagingConfig(
            pageSize = ComicsPagingSource.PAGE_SIZE,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { createPagingSource() },
    ).flow

    private fun createPagingSource() = ComicsPagingSource(database, gson) { offset ->
        service.comics(offset).also { response ->
            assert(response.code == 200) { response }
            response.copyright?.let { copyright.value = it }
            attribution.value = response.attributionText
        }
    }

}
