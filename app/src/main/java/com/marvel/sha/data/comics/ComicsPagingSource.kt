package com.marvel.sha.data.comics

import androidx.paging.PagingSource
import androidx.paging.PagingState
import bx.logging.Log
import bx.system.Cx
import com.google.gson.Gson
import com.marvel.sha.data.MarvelData
import com.marvel.sha.data.MarvelResponse
import com.marvel.sha.data.fromSqlDelight
import com.marvel.sha.data.int
import com.marvel.sha.domain.MarvelComic
import commarvel.sha.datacomics.ComicsQueries
import java.io.IOException
import kotlinx.coroutines.withContext
import retrofit2.HttpException

internal class ComicsPagingSource(
    private val dao: ComicsQueries,
    private val gson: Gson,
    private val load: suspend (Int) -> MarvelResponse,
) : PagingSource<Int, MarvelComic>() {

    private fun MarvelData.isLastPage() = count == 0

    override fun getRefreshKey(state: PagingState<Int, MarvelComic>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MarvelComic> = try {
        val offset = params.key ?: 0
        val (data, entities) = withContext(Cx.IO) { page(offset) }
        Log.info { data.copy(results = emptyList()) }
        val prev = (offset - data.limit).coerceAtLeast(0)
        val next = (offset + data.count)
        LoadResult.Page(
            data = entities,
            prevKey = if (offset == 0) null else prev,
            nextKey = if (data.isLastPage()) null else next,
        )
    }
    catch (exception: IOException) {
        LoadResult.Error(exception)
    }
    catch (exception: HttpException) {
        LoadResult.Error(exception)
    }

    private suspend fun page(offset: Int): Pair<MarvelData, List<MarvelComic>> {

        var envelope: MarvelData? = null

        val cachedCount = dao.count().executeAsOne()
        Log.info("cached count from database: $cachedCount")
        if (cachedCount < offset + PAGE_SIZE) {

            Log.info("network load at offset: $offset")

            val response = load(offset)
            assert(response.code == 200) { response }
            envelope = response.data

            Log.info("network response: ${response.data.copy(results = emptyList())}")

            dao.transaction {
                val entities = response.data.results
                Log.info("store ${entities.size} entities into database")
                entities.forEachIndexed { idx, it ->
                    val id = it.int("id")
                    dao.insert((offset + idx).toLong(), id.toLong(), gson.toJson(it))
                }
            }
        }

        Log.info("slurp from database $offset .. ${offset.plus(PAGE_SIZE)}")
        val entities = dao.paged(offset.toLong(), offset + PAGE_SIZE.toLong())
            .executeAsList()
            .map { gson.fromSqlDelight(it) }

        Log.warn { "slurped ${entities.size} entities" }
        envelope = envelope ?: MarvelData(offset, PAGE_SIZE, 0, entities.size, emptyList())

        Log.info("resulting envelope ${envelope.copy(results = emptyList())}")

        return envelope to entities
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
