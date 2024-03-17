package com.marvel.sha.data.ktordelight

import androidx.paging.*
import app.cash.sqldelight.paging3.QueryPagingSource
import bx.system.Cx
import com.google.gson.Gson
import com.marvel.sha.data.fromSqlDelight
import com.marvel.sha.domain.MarvelComic
import commarvel.sha.datacomics.ComicsQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("MemberVisibilityCanBePrivate") @OptIn(ExperimentalPagingApi::class)
internal class ComicRepository(
    private val database: ComicsQueries,
    private val service: ComicService,
    private val gson: Gson,
) {
    @Suppress("RedundantSuspendModifier")
    suspend fun comicDetail(characterId: String) = database.byId(characterId.toLong()).executeAsOne()
        .let(gson::fromSqlDelight)

    fun comics(query: String): Flow<PagingData<MarvelComic>> {
        val mediator = ComicRemoteMediator(database, service, gson, query)
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = mediator,
            pagingSourceFactory = {
                QueryPagingSource(
                    countQuery = database.count(),
                    transacter = database,
                    context = Cx.IO,
                    queryProvider = database::paged,
                ).also { mediator.source = it }
            },
        ).flow.map { data -> data.map(gson::fromSqlDelight) }
    }
}
